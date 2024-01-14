/* (C)2024 */
package pl.ninecube.oss.cakecdn.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.model.domain.ApplicationToken;
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

        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (applicationTokenRepository.existsByToken(uuid.toString()));

        if (!projectRepository.existsById(createTokenDto.getProjectId()))
            throw new BusinessException("Project not exist");

        ApplicationToken applicationToken =
                ApplicationToken.builder()
                        .token(uuid.toString())
                        .applicationName(createTokenDto.getApplicationName())
                        .projectId(createTokenDto.getProjectId())
                        .build();

        ApplicationTokenEntity applicationTokenEntity =
                applicationTokenRepository.save(applicationTokenMapper.toEntity(applicationToken));

        return applicationTokenMapper.toResponse(applicationTokenEntity);
    }

    public Set<TokenResponse> getTokens(Long projectId) {
        return applicationTokenRepository.findAllByProjectId(projectId).stream()
                .map(applicationTokenMapper::toResponse)
                .collect(Collectors.toSet());
    }

    public void deleteToken(Long tokenId) {
        applicationTokenRepository.deleteById(tokenId);
    }
}
