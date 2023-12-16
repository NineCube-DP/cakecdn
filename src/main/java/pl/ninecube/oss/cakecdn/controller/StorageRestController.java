package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.ninecube.oss.cakecdn.model.dto.*;
import pl.ninecube.oss.cakecdn.service.ProjectService;
import pl.ninecube.oss.cakecdn.service.StorageService;

import java.io.InputStream;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@Tag(name = "Managing files in project")
@SecurityRequirement(name = "basicAuth")
public class StorageRestController {

    private final StorageService storageService;

    @PostMapping("/bucket")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new bucket")
    public BucketResponse createNewBucket(@Valid @RequestBody BucketCreateDto dto) {
        return storageService.createBucket(dto);
    }

    @GetMapping("/bucket/{bucketId}")
    @Operation(summary = "Get bucket info by bucketId")
    public BucketResponse getBucketMetadata(@PathVariable Long bucketId) {
        return storageService.getBucketMetadata(bucketId);
    }

    @DeleteMapping("/bucket/{bucketId}")
    @Operation(summary = "Delete bucket by id")
    public void deleteProject(@PathVariable Long bucketId) {
        storageService.deleteBucket(bucketId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save new file")
    public StorageResponse saveNewFile(@RequestParam Long bucketId,
                                       @Valid @RequestBody StorageCreateDto dto,
                                       @RequestParam("file") MultipartFile file) {
        return storageService.saveFile(bucketId, dto, file);
    }

    @GetMapping("/{storageId}")
    @Operation(summary = "Get bytes of file by storageId")
    public byte[] returnFile(@PathVariable Long storageId) {
        return storageService.getFile(storageId);
    }

    @GetMapping("/{storageId}/metadata")
    @Operation(summary = "Get bytes of file by storageId")
    public StorageResponse readProject(@PathVariable Long storageId) {
        return storageService.getFileMetadata(storageId);
    }

    @DeleteMapping("/{storageId}")
    @Operation(summary = "Delete project by id")
    public void deleteProject(@RequestParam Long bucketId, @PathVariable Long storageId) {
        storageService.deleteFile(storageId);
    }
}
