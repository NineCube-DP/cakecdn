package pl.ninecube.oss.cakecdn.service;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.exception.TechnicalException;
import pl.ninecube.oss.cakecdn.model.domain.Project;
import pl.ninecube.oss.cakecdn.model.domain.Storage;
import pl.ninecube.oss.cakecdn.model.domain.Item;
import pl.ninecube.oss.cakecdn.model.dto.StorageCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
import pl.ninecube.oss.cakecdn.model.dto.ItemCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.ItemResponse;
import pl.ninecube.oss.cakecdn.model.entity.ProjectEntity;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;
import pl.ninecube.oss.cakecdn.model.mapper.ProjectMapper;
import pl.ninecube.oss.cakecdn.model.mapper.StorageMapper;
import pl.ninecube.oss.cakecdn.repository.ProjectRepository;
import pl.ninecube.oss.cakecdn.repository.StorageRepository;
import pl.ninecube.oss.cakecdn.repository.ItemRepository;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient client;
    private final StorageRepository storageRepository;
    private final ItemRepository itemRepository;
    private final ProjectRepository projectRepository;
    private final StorageMapper storageMapper;
    private final ProjectMapper projectMapper;

    @SneakyThrows
    public StorageResponse createBucket(Long projectId, StorageCreateDto dto) {
        if (isBucketExist(dto.getName())) {
            StorageEntity storageEntity = storageRepository.findByName(dto.getName())
                    .orElseThrow(() -> new TechnicalException("Bucket not found"));

            return storageMapper.toResponse(storageEntity);
        }

        ProjectEntity projectEntity = projectRepository.findById(projectId)
                .orElseThrow(() -> new BusinessException("Project not found"));
        Project project = projectMapper.toDomain(projectEntity);

        Storage storage = Storage.builder()
                .name(dto.getName())
                .project(project)
                .build();

        client.makeBucket(MakeBucketArgs.builder()
                .bucket(storage.getName())
                .build());

        StorageEntity storageEntity = storageMapper.toEntity(storage);

        storageRepository.save(storageEntity);

        return storageMapper.toResponse(storageEntity);
    }

    private boolean isBucketExist(String name) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        return client.bucketExists(BucketExistsArgs.builder()
                .bucket(name)
                .build());
    }

    public StorageResponse getBucketMetadata(Long bucketId) {
        StorageEntity storageEntity = storageRepository.findById(bucketId)
                .orElseThrow(() -> new BusinessException("Bucket not exist"));

        return storageMapper.toResponse(storageEntity);
    }

    @SneakyThrows
    public void deleteBucket(Long bucketId) {
        StorageEntity storageEntity = storageRepository.findById(bucketId)
                .orElseThrow(() -> new BusinessException("Bucket not exist"));

        if (isBucketExist(storageEntity.getName())) {
            client.removeBucket(RemoveBucketArgs.builder()
                    .bucket(storageEntity.getName())
                    .build());
        }

        storageRepository.delete(storageEntity);
    }

    @SneakyThrows
    public ItemResponse saveFile(Long bucketId, ItemCreateDto dto, MultipartFile file) {
        var storageEntity = storageRepository.findById(bucketId)
                .orElseThrow(() -> new BusinessException("Bucket not exist"));

        var storage = storageMapper.toDomain(storageEntity);

        var item = Item.builder()
                .fileName(dto.getFileName())
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .storage(storage)
                .uuid(UUID.randomUUID().toString())
                .tags(dto.getTags())
                .categories(dto.getCategories())
                .parameters(dto.getParameters())
                .build();

//        bucket.getFiles().add(storage);

        if (isBucketExist(storage.getName())) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(item.getUuid())
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        }

        return null;
    }

    public byte[] getFile(Long storageId) {
        return null;
    }

    public ItemResponse getFileMetadata(Long storageId) {
        return null;
    }

    public void deleteFile(Long bucketId, Long storageId) {

    }
}
