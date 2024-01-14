/* (C)2024 */
package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
import pl.ninecube.oss.cakecdn.service.ApplicationTokenService;
import pl.ninecube.oss.cakecdn.service.StorageService;

@RestController
@RequestMapping("/cdn")
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
        return storageService.getStorageMetadataByName(storageName);
    }

    @GetMapping("/{storageName}/{itemUuid}")
    @Operation(summary = "Get bytes of file by itemId")
    public byte[] getFile(@PathVariable String storageName, @PathVariable String itemUuid) {
        return storageService.getFile(storageName, itemUuid);
    }
}
