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
import pl.ninecube.oss.cakecdn.model.dto.ItemResponse;
import pl.ninecube.oss.cakecdn.model.dto.ItemUpdateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
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
  public StorageResponse createNewBucket(
          @RequestParam Long projectId, @Valid @RequestBody StorageCreateDto dto) {
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
  public ItemResponse saveNewFile(
          @RequestParam Long storageId, @RequestPart(value = "file") MultipartFile file) {
    return storageService.saveFile(storageId, file);
  }

  @GetMapping("/item/{itemId}")
  @Operation(summary = "Get bytes of file by itemId")
  public byte[] returnFile(@PathVariable Long itemId) {
    // todo return file name from metadata
    return storageService.getFile(itemId);
  }

  @GetMapping("/{storageName}/{itemUuid}")
  @Operation(summary = "Get bytes of file by itemId")
  public byte[] returnFileFullPath(
          @PathVariable String storageName, @PathVariable String itemUuid) {
    // todo return file name from metadata
    return storageService.getFile(storageName, itemUuid);
  }

  @DeleteMapping("/item/{itemId}")
  @Operation(summary = "Delete file by itemId")
  public void deleteFile(@PathVariable Long itemId) {
    storageService.deleteFile(itemId);
  }

  @GetMapping("/item/{itemId}/metadata")
  @Operation(summary = "Get file metadata by itemId")
  public ItemResponse readItemMetadata(@PathVariable Long itemId) {
    return storageService.getFileMetadata(itemId);
  }

  @PutMapping("/item/{itemId}/metadata")
  @Operation(summary = "Update file metadata by itemId")
  public ItemResponse updateItemMetadata(
          @PathVariable Long itemId, @Valid @RequestBody ItemUpdateDto dto) {
    return storageService.updateMetadata(itemId, dto);
  }
}
