package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.ninecube.oss.cakecdn.model.dto.*;
import pl.ninecube.oss.cakecdn.service.StorageService;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
@Tag(name = "Managing files and storages in projects")
@SecurityRequirement(name = "basicAuth")
public class StorageRestController {

  private final StorageService storageService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create new storage")
  public StorageResponse createNewBucket(@RequestParam Long projectId, @Valid @RequestBody StorageCreateDto dto) {
    return storageService.createBucket(projectId, dto);
  }

  @GetMapping("/{storageId}")
  @Operation(summary = "Get storage info by storageId")
  public StorageResponse getBucketMetadata(@PathVariable Long storageId) {
    return storageService.getBucketMetadata(storageId);
  }

  @DeleteMapping("/{storageId}")
  @Operation(summary = "Delete storage by id")
  public void deleteProject(@PathVariable Long storageId) {
    storageService.deleteBucket(storageId);
  }

  // fixme - fix upload file with json
  @PostMapping(value = "/item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Save new file")
  public ItemResponse saveNewFile(@RequestParam Long storageId,
                                  @RequestPart(value = "file") MultipartFile file) {
    return storageService.saveFile(storageId, file);
  }

  @GetMapping("/item/{itemId}")
  @Operation(summary = "Get bytes of file by itemId")
  public byte[] returnFile(@PathVariable Long itemId) {
    return storageService.getFile(itemId);
  }

  @PostMapping("/item/{itemId}/metadata")
  @Operation(summary = "Save metadata for item by id")
  public ItemResponse readProject(@PathVariable Long itemId, @Valid @RequestBody ItemCreateDto dto) {
    return storageService.saveMetadata(itemId, dto);
  }

  @GetMapping("/item/{itemId}/metadata")
  @Operation(summary = "Get bytes of file by itemId")
  public ItemResponse readProject(@PathVariable Long itemId) {
    return storageService.getFileMetadata(itemId);
  }

  @PutMapping("/item/{itemId}/metadata")
  @Operation(summary = "Get bytes of file by itemId")
  public ItemResponse readProject(@PathVariable Long itemId, @Valid @RequestBody ItemUpdateDto dto) {
    return storageService.updateMetadata(itemId, dto);
  }

  @DeleteMapping("/{itemId}")
  @Operation(summary = "Delete project by id")
  public void deleteProject(@RequestParam Long bucketId, @PathVariable Long itemId) {
    storageService.deleteFile(bucketId, itemId);
  }
}
