/* (C)2023 */
package pl.ninecube.oss.cakecdn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.model.dto.ProjectCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.ProjectResponse;
import pl.ninecube.oss.cakecdn.model.dto.ProjectUpdateDto;
import pl.ninecube.oss.cakecdn.model.mapper.AccountMapper;
import pl.ninecube.oss.cakecdn.model.mapper.ProjectMapper;
import pl.ninecube.oss.cakecdn.repository.AccountRepository;
import pl.ninecube.oss.cakecdn.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AccountMapper accountMapper;

    public ProjectResponse createProject(ProjectCreateDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        var accountEntity =
                accountRepository
                        .findByUsername(auth.getName())
                        .orElseThrow(() -> new BusinessException("User not exists by Username"));

        var project = projectMapper.toDomain(dto);
        project.setOwner(accountMapper.toDomain(accountEntity));
        // initially project is disabled
        project.setEnabled(false);

        var entity = projectRepository.save(projectMapper.toEntity(project));

        return projectMapper.toResponse(entity);
    }

    public ProjectResponse getProjectById(Long projectId) {
        var entity =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(() -> new BusinessException("Account not found"));
        return projectMapper.toResponse(entity);
    }

    public ProjectResponse updateProjectById(Long projectId, ProjectUpdateDto dto) {
        var entity =
                projectRepository
                        .findById(projectId)
                        .orElseThrow(() -> new BusinessException("Account not found"));

        var updated = projectMapper.update(projectMapper.toDomain(entity), dto);

        var updatedEntity = projectRepository.save(projectMapper.toEntity(updated));

        return projectMapper.toResponse(updatedEntity);
    }

    public void deleteProjectById(Long projectId) {
        projectRepository.deleteById(projectId);
    }
}
