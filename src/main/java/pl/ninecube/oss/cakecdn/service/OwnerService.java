package pl.ninecube.oss.cakecdn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.model.dto.OwnerCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.OwnerResponse;
import pl.ninecube.oss.cakecdn.model.dto.OwnerUpdateDto;
import pl.ninecube.oss.cakecdn.model.entity.OwnerEntity;
import pl.ninecube.oss.cakecdn.model.mapper.OwnerMapper;
import pl.ninecube.oss.cakecdn.repository.OwnerRepository;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerResponse saveOwner(OwnerCreateDto dto) {
        OwnerEntity entity = ownerRepository.save(ownerMapper.toEntity(dto));

        return ownerMapper.toResponse(entity);
    }

    public OwnerResponse getOwnerById(Long ownerId) {
        OwnerEntity entity = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new BusinessException("Owner not found"));
        return ownerMapper.toResponse(entity);
    }

    public OwnerResponse updateOwnerById(Long ownerId, OwnerUpdateDto dto) {
        return null;
    }

    public void deleteOwner(Long ownerId) {
        ownerRepository.deleteById(ownerId);
    }
}