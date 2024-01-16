/* (C)2024 */
package pl.ninecube.oss.cakecdn.service;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.exception.ResourceNotExistException;
import pl.ninecube.oss.cakecdn.model.domain.ApplicationToken;
import pl.ninecube.oss.cakecdn.model.domain.Owner;
import pl.ninecube.oss.cakecdn.model.dto.CreateTokenDto;
import pl.ninecube.oss.cakecdn.model.dto.TokenResponse;
import pl.ninecube.oss.cakecdn.model.entity.ApplicationTokenEntity;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;
import pl.ninecube.oss.cakecdn.model.mapper.ApplicationTokenMapper;
import pl.ninecube.oss.cakecdn.repository.ApplicationTokenRepository;
import pl.ninecube.oss.cakecdn.repository.ProjectRepository;
import pl.ninecube.oss.cakecdn.repository.StorageRepository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationTokenService {

    private final ProjectRepository projectRepository;
    private final StorageRepository storageRepository;
    private final ApplicationTokenMapper applicationTokenMapper;
    private final ApplicationTokenRepository applicationTokenRepository;


    public boolean isValid(String applicationToken, String storageName) {
        return applicationTokenRepository
                .findByToken(applicationToken)
                .map(tokenEntity -> verifyOwner(tokenEntity, storageName))
                .orElse(false);
    }

    private boolean verifyOwner(ApplicationTokenEntity token, String storageName) {
        return storageRepository.findByProjectId(token.getProjectId()).stream()
                .map(StorageEntity::getName)
                .anyMatch(s -> s.equals(storageName));
    }

    public TokenResponse createToken(CreateTokenDto createTokenDto) {

        String uuid = getNonExistingUuid();

        if (!projectRepository.existsById(createTokenDto.getProjectId()))
            throw new ResourceNotExistException("Project not exist");

        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ApplicationToken applicationToken =
                ApplicationToken.builder()
                        .owner(owner)
                        .token(uuid)
                        .applicationName(createTokenDto.getApplicationName())
                        .projectId(createTokenDto.getProjectId())
                        .build();

        ApplicationTokenEntity applicationTokenEntity1 = applicationTokenMapper.toEntity(applicationToken);

        ApplicationTokenEntity applicationTokenEntity = applicationTokenRepository.save(applicationTokenEntity1);

        return applicationTokenMapper.toResponse(applicationTokenEntity);
    }

    @NotNull
    private String getNonExistingUuid() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (applicationTokenRepository.existsByToken(uuid.toString()));
        return uuid.toString();
    }

    public Set<TokenResponse> getTokens(Long projectId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return applicationTokenRepository.findAllByProjectIdAndOwnerId(projectId, owner.getId()).stream()
                .map(applicationTokenMapper::toResponse)
                .collect(Collectors.toSet());
    }

    public void deleteToken(Long tokenId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApplicationTokenEntity token = applicationTokenRepository.findByIdAndOwnerId(tokenId, owner.getId())
                .orElseThrow(() -> new ResourceNotExistException("Token not exist"));

        applicationTokenRepository.delete(token);
    }

    public Long getOwner(String applicationToken) {
        ApplicationTokenEntity token = applicationTokenRepository.findByToken(applicationToken)
                .orElseThrow(() -> new ResourceNotExistException("Token not exist"));

        return token.getOwnerId();
    }
}
