/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.ninecube.oss.cakecdn.model.dto.ProjectCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.ProjectResponse;
import pl.ninecube.oss.cakecdn.model.dto.ProjectUpdateDto;
import pl.ninecube.oss.cakecdn.service.ProjectService;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
@Tag(name = "Managing project")
@SecurityRequirement(name = "basicAuth")
public class ProjectRestController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save new project")
    public ProjectResponse createProject(@Valid @RequestBody ProjectCreateDto dto) {
        return projectService.createProject(dto);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Get project by id")
    public ProjectResponse readProject(@PathVariable Long projectId) {
        return projectService.getProjectById(projectId);
    }

    @GetMapping
    @Operation(summary = "Get all projects")
    public List<ProjectResponse> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/search")
    @Operation(summary = "Find projects by name")
    public List<ProjectResponse> searchProject(@RequestParam String projectName) {
        return projectService.getProjectByName(projectName);
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Update project data by id")
    public ProjectResponse updateProject(
            @PathVariable Long projectId, @RequestBody ProjectUpdateDto dto) {
        return projectService.updateProjectById(projectId, dto);
    }

    @DeleteMapping("/{projectId}")
    @Operation(summary = "Delete project by id")
    public void deleteProject(@PathVariable Long projectId) {
        projectService.deleteProjectById(projectId);
    }
}
