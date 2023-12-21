package pl.ninecube.oss.cakecdn.service;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.exception.TechnicalException;
import pl.ninecube.oss.cakecdn.model.domain.Item;
import pl.ninecube.oss.cakecdn.model.domain.Project;
import pl.ninecube.oss.cakecdn.model.domain.Storage;
import pl.ninecube.oss.cakecdn.model.dto.ItemResponse;
import pl.ninecube.oss.cakecdn.model.dto.ItemUpdateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;
import pl.ninecube.oss.cakecdn.model.entity.ProjectEntity;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;
import pl.ninecube.oss.cakecdn.model.mapper.ItemMapper;
import pl.ninecube.oss.cakecdn.model.mapper.ProjectMapper;
import pl.ninecube.oss.cakecdn.model.mapper.StorageMapper;
import pl.ninecube.oss.cakecdn.repository.ItemRepository;
import pl.ninecube.oss.cakecdn.repository.ProjectRepository;
import pl.ninecube.oss.cakecdn.repository.StorageRepository;

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
    private final ItemMapper itemMapper;
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
    public ItemResponse saveFile(Long bucketId, MultipartFile file) {
        var storageEntity = storageRepository.findById(bucketId)
                .orElseThrow(() -> new BusinessException("Bucket not exist"));

        var storage = storageMapper.toDomain(storageEntity);
        var uuid = UUID.randomUUID().toString();
        var item = Item.builder()
//                .fileName(dto.getFileName())
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSize(file.getSize())
                .storage(storage)
                .uuid(uuid)
                .url(storage.getProject().getBaseUrl() + "/" + storage.getName() + "/" + uuid)
//                .tags(dto.getTags())
//                .categories(dto.getCategories())
//                .parameters(dto.getParameters())
                .build();

//        bucket.getFiles().add(storage);

        if (isBucketExist(storage.getName())) {
            client.putObject(PutObjectArgs.builder()
                    .object(item.getUuid())
                    .bucket(item.getStorage().getName())
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        }

        var entity = itemRepository.save(itemMapper.toEntity(item));

        return itemMapper.toResponse(entity);
    }

    public byte[] getFile(Long storageId) {
        return null;
    }

    public ItemResponse getFileMetadata(Long storageId) {
        return null;
    }

    public void deleteFile(Long bucketId, Long storageId) {

    }

    public ItemResponse saveMetadata(Long itemId, ItemUpdateDto dto) {
        ItemEntity file = itemRepository.findById(itemId)
                .orElseThrow(() -> new BusinessException("File not exist"));

        Item domain = itemMapper.toDomain(file);

        Item update = itemMapper.update(domain, dto);

        ItemEntity entity = itemMapper.toEntity(update);

        ItemEntity save = itemRepository.save(entity);

        return itemMapper.toResponse(save);
    }

    public ItemResponse updateMetadata(Long itemId, ItemUpdateDto dto) {
        return null;
    }
}
