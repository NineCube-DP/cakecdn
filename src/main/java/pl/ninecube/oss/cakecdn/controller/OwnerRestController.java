package pl.ninecube.oss.cakecdn.controller;

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
public class OwnerRestController {

    private final OwnerService ownerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OwnerResponse createOwner(@Valid @RequestBody OwnerCreateDto dto) {
        return ownerService.saveOwner(dto);
    }

    @GetMapping("/{ownerId}")
    public OwnerResponse readOwner(@PathVariable Long ownerId) {
        return ownerService.getOwnerById(ownerId);
    }

    @PutMapping("/{ownerId}")
    public OwnerResponse updateOwner(@PathVariable Long ownerId, OwnerUpdateDto dto) {
        return ownerService.updateOwnerById(ownerId, dto);
    }

    @DeleteMapping("/ownerId")
    public void deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
    }
}
