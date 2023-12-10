package pl.ninecube.oss.cakecdn.controller;

import lombok.RequiredArgsConstructor;
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
    public OwnerResponse createOwner(OwnerCreateDto dto) {
        return ownerService.saveOwner(dto);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public OwnerResponse readOwner(@PathVariable Long ownerId) {
        return ownerService.getOwnerById(ownerId);
    }
    @PutMapping(value = "/{id}", produces = "application/json")
    public OwnerResponse updateOwner(@PathVariable Long ownerId, OwnerUpdateDto dto) {
        return ownerService.updateOwnerById(ownerId, dto);
    }

    @DeleteMapping("/id")
    public void deleteOwner(@PathVariable Long ownerId) {
        ownerService.deleteOwner(ownerId);
    }
}
