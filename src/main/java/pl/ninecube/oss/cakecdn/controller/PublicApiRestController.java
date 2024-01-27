/* (C)2024 */
package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.model.domain.File;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
import pl.ninecube.oss.cakecdn.service.ApplicationTokenService;
import pl.ninecube.oss.cakecdn.service.StorageService;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "Api for direct use")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class PublicApiRestController {

    private final StorageService storageService;
    private final ApplicationTokenService applicationTokenService;

    @GetMapping("/{storageName}")
    @Operation(summary = "Return information about storage and files in it")
    public StorageResponse getStorageMetadata(
            @PathVariable String storageName, @RequestParam String applicationToken) {
        if (!applicationTokenService.isValid(applicationToken, storageName))
            throw new BusinessException("No application token is provided or token is wrong");
        Long ownerId = applicationTokenService.getOwner(applicationToken);
        return storageService.getStorageMetadataByName(storageName, ownerId);
    }

    @GetMapping("/{storageName}/{itemUuid}")
    @Operation(summary = "Get bytes of file by itemId")
    public ResponseEntity<Resource> getFile(@PathVariable String storageName, @PathVariable String itemUuid) {
        File file = storageService.getFile(storageName, itemUuid);

        ByteArrayResource resource = new ByteArrayResource(file.getPayload());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(file.getFileName())
                                .build().toString())
                .body(resource);
    }
}
