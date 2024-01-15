/* (C)2023 */
package pl.ninecube.oss.cakecdn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.exception.ResourceNotExistException;
import pl.ninecube.oss.cakecdn.model.domain.Owner;
import pl.ninecube.oss.cakecdn.model.domain.Project;
import pl.ninecube.oss.cakecdn.model.dto.ProjectCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.ProjectResponse;
import pl.ninecube.oss.cakecdn.model.dto.ProjectUpdateDto;
import pl.ninecube.oss.cakecdn.model.mapper.ProjectMapper;
import pl.ninecube.oss.cakecdn.repository.ProjectRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(ProjectCreateDto dto) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var project = projectMapper.toDomain(dto);

        project.setOwner(owner);

        // initially project is disabled
        project.setEnabled(false);

        var entity = projectRepository.save(projectMapper.toEntity(project));

        return projectMapper.toResponse(entity);
    }

    public ProjectResponse getProjectById(Long projectId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity =
                projectRepository
                        .findByIdAndOwnerId(projectId, owner.getId())
                        .orElseThrow(() -> new ResourceNotExistException("Project not found"));

        return projectMapper.toResponse(entity);
    }

    public ProjectResponse updateProjectById(Long projectId, ProjectUpdateDto dto) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity =
                projectRepository
                        .findByIdAndOwnerId(projectId, owner.getId())
                        .orElseThrow(() -> new ResourceNotExistException("Project not found"));

        Project domain = projectMapper.toDomain(entity);
        domain.setOwner(owner);

        var updated = projectMapper.update(domain, dto);

        var updatedEntity = projectRepository.save(projectMapper.toEntity(updated));

        return projectMapper.toResponse(updatedEntity);
    }

    public void deleteProjectById(Long projectId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        projectRepository.deleteByIdAndOwnerId(projectId, owner.getId());
    }

    public List<ProjectResponse> getProjectByName(String projectName) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity = projectRepository.findAllByNameContainingIgnoreCaseAndOwnerId(projectName, owner.getId());
        return entity.stream()
                .map(projectMapper::toResponse)
                .sorted(Comparator.comparing(ProjectResponse::getId))
                .collect(Collectors.toList());
    }
}
