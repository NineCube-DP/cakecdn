package pl.ninecube.oss.cakecdn.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.ninecube.oss.cakecdn.model.dto.OwnerCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.OwnerResponse;
import pl.ninecube.oss.cakecdn.model.dto.OwnerUpdateDto;
import pl.ninecube.oss.cakecdn.service.OwnerService;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
@Tag(name = "Managing owners")
public class OwnerRestController {

    private final OwnerService ownerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save new owner")
    public OwnerResponse createOwner(@Valid @RequestBody OwnerCreateDto dto) {
        return ownerService.saveOwner(dto);
    }

    @GetMapping("/{ownerId}")
    @Operation(summary = "Get owner by id")
    public OwnerResponse readOwner(@PathVariable Long ownerId) {
        return ownerService.getOwnerById(ownerId);
    }

    @PutMapping("/{ownerId}")
    @Operation(summary = "Update owner data by id")
    public OwnerResponse updateOwner(@PathVariable Long ownerId, OwnerUpdateDto dto) {
        return ownerService.updateOwnerById(ownerId, dto);
    }

    @DeleteMapping("/{ownerId}")
    @Operation(summary = "Delete owner by id")
    public void deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
    }
}
