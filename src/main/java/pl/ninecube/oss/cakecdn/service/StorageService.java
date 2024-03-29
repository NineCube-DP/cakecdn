/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.service;

import io.minio.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.exception.ResourceNotExistException;
import pl.ninecube.oss.cakecdn.model.domain.File;
import pl.ninecube.oss.cakecdn.model.domain.Item;
import pl.ninecube.oss.cakecdn.model.domain.Owner;
import pl.ninecube.oss.cakecdn.model.domain.Storage;
import pl.ninecube.oss.cakecdn.model.dto.*;
import pl.ninecube.oss.cakecdn.model.entity.ItemEntity;
import pl.ninecube.oss.cakecdn.model.entity.ProjectEntity;
import pl.ninecube.oss.cakecdn.model.entity.StorageEntity;
import pl.ninecube.oss.cakecdn.model.mapper.ItemMapper;
import pl.ninecube.oss.cakecdn.model.mapper.StorageMapper;
import pl.ninecube.oss.cakecdn.repository.ItemRepository;
import pl.ninecube.oss.cakecdn.repository.ProjectRepository;
import pl.ninecube.oss.cakecdn.repository.StorageRepository;
import pl.ninecube.oss.cakecdn.repository.query.ItemQuery;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient client;
    private final StorageRepository storageRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ProjectRepository projectRepository;
    private final StorageMapper storageMapper;
    private final ItemQuery itemQuery;

    @SneakyThrows
    public StorageResponse createStorage(Long projectId, StorageCreateDto dto) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ProjectEntity projectEntity =
                projectRepository
                        .findByIdAndOwnerId(projectId, owner.getId())
                        .orElseThrow(() -> new ResourceNotExistException("Project not found"));

        String bucketName = getUuidForStorage();

        Storage storage =
                Storage.builder()
                        .name(dto.getName())
                        .bucketName(bucketName)
                        .projectId(projectEntity.getId())
                        .owner(owner)
                        .build();

        if (isBucketExist(storage.getBucketName())) {
            throw new BusinessException("Bucket already exist");
        }

        client.makeBucket(MakeBucketArgs.builder().bucket(storage.getBucketName()).build());

        StorageEntity savedStorage = storageRepository.save(storageMapper.toEntity(storage));

        return storageMapper.toResponse(savedStorage);
    }

    @SneakyThrows
    private boolean isBucketExist(String name) {
        return client.bucketExists(BucketExistsArgs.builder().bucket(name).build());
    }

    @Transactional
    public StorageResponse getStorageMetadata(Long bucketId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StorageEntity storageEntity =
                storageRepository
                        .findByIdAndOwnerId(bucketId, owner.getId())
                        .orElseThrow(() -> new BusinessException("Bucket not exist"));

        List<ItemEntity> items = itemRepository.findByStorageId(bucketId);
        Storage storage = storageMapper.toDomain(storageEntity);
        storage.setItems(items.stream().map(itemMapper::toDomain).collect(Collectors.toList()));

        return storageMapper.toResponse(storage);
    }

    @Transactional
    public StorageResponse getStorageMetadataByName(String storageName, Long ownerId) {
        StorageEntity storageEntity =
                storageRepository
                        .findByNameAndOwnerId(storageName, ownerId)
                        .orElseThrow(() -> new BusinessException("Bucket not exist"));

        List<ItemEntity> items = itemRepository.findByStorageId(storageEntity.getId());
        Storage storage = storageMapper.toDomain(storageEntity);
        storage.setItems(items.stream().map(itemMapper::toDomain).collect(Collectors.toList()));

        return storageMapper.toResponse(storage);
    }

    public List<StorageResponse> findStorageMetadatasByName(String storageName) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity =
                storageRepository.findAllByNameContainingIgnoreCaseAndOwnerId(
                        storageName, owner.getId());

        return entity.stream()
                .map(storageMapper::toDomain)
                .map(storageMapper::toResponse)
                .sorted(Comparator.comparing(StorageResponse::getId))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    public void deleteStorage(Long bucketId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        StorageEntity storageEntity =
                storageRepository
                        .findByIdAndOwnerId(bucketId, owner.getId())
                        .orElseThrow(() -> new BusinessException("Bucket not exist"));

        Storage storage = storageMapper.toDomain(storageEntity);

        if (isBucketExist(storage.getBucketName())) {
            client.removeBucket(RemoveBucketArgs.builder().bucket(storage.getBucketName()).build());
        }

        storageRepository.delete(storageEntity);
    }

    // TODO: 17/01/2024 to refactor
    @NotNull
    private String getUuidForStorage() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (storageRepository.existsByBucketName(uuid.toString()));
        return uuid.toString();
    }

    @NotNull
    private String getUuidForItem() {
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (itemRepository.existsByUuid(uuid.toString()));
        return uuid.toString();
    }

    @SneakyThrows
    public ItemResponse saveFile(Long bucketId, MultipartFile file) {
        var owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var storageEntity =
                storageRepository
                        .findByIdAndOwnerId(bucketId, owner.getId())
                        .orElseThrow(() -> new BusinessException("Bucket not exist"));

        var storage = storageMapper.toDomain(storageEntity);

        var uuid = getUuidForItem();

        var projectEntity =
                projectRepository
                        .findById(storage.getProjectId())
                        .orElseThrow(() -> new BusinessException("Project not exist"));

        var item =
                Item.builder()
                        .owner(owner)
                        .originalFileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .fileSize(file.getSize())
                        .storageId(storage.getId())
                        .uuid(uuid)
                        .url(
                                projectEntity.getBaseUrl().replaceAll("/$", "")
                                        + "/files/"
                                        + storage.getName()
                                        + "/"
                                        + uuid)
                        .build();

        if (isBucketExist(storage.getBucketName())) {
            client.putObject(
                    PutObjectArgs.builder()
                            .object(item.getUuid())
                            .bucket(storage.getBucketName())
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
        }

        var entity = itemRepository.save(itemMapper.toEntity(item));

        return itemMapper.toResponse(entity);
    }

    @SneakyThrows
    public byte[] getFile(Long itemId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ItemEntity file =
                itemRepository
                        .findByIdAndOwnerId(itemId, owner.getId())
                        .orElseThrow(() -> new BusinessException("File not exist"));

        StorageEntity storageEntity =
                storageRepository
                        .findByIdAndOwnerId(file.getStorageId(), owner.getId())
                        .orElseThrow(() -> new BusinessException("Storage not exist"));

        Storage storage = storageMapper.toDomain(storageEntity);

        return client.getObject(
                        GetObjectArgs.builder()
                                .bucket(storage.getBucketName())
                                .object(file.getUuid())
                                .build())
                .readAllBytes();
    }

    @SneakyThrows
    public File getFile(String storageName, String itemUuid) {
        // todo consider secure download file with app token
        StorageEntity storageEntity =
                storageRepository
                        .findByName(storageName)
                        .orElseThrow(() -> new BusinessException("Storage not exist"));

        Storage storage = storageMapper.toDomain(storageEntity);

        ItemEntity file =
                itemRepository
                        .findByStorageIdAndUuid(storage.getId(), itemUuid)
                        .orElseThrow(() -> new BusinessException("File not exist"));

        return File.builder()
                .payload(
                        client.getObject(
                                        GetObjectArgs.builder()
                                                .bucket(storage.getBucketName())
                                                .object(file.getUuid())
                                                .build())
                                .readAllBytes())
                .fileName(file.getOriginalFileName())
                .build();
    }

    @Transactional
    public ItemResponse getFileMetadata(Long itemId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ItemEntity file =
                itemRepository
                        .findByIdAndOwnerId(itemId, owner.getId())
                        .orElseThrow(() -> new BusinessException("File not exist"));

        return itemMapper.toResponse(file);
    }

    @SneakyThrows
    public void deleteFile(Long itemId) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ItemEntity file =
                itemRepository
                        .findByIdAndOwnerId(itemId, owner.getId())
                        .orElseThrow(() -> new BusinessException("File not exist"));

        StorageEntity storageEntity =
                storageRepository
                        .findByIdAndOwnerId(file.getStorageId(), owner.getId())
                        .orElseThrow(() -> new BusinessException("Storage not exist"));

        Storage storage = storageMapper.toDomain(storageEntity);

        client.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(storage.getBucketName())
                        .object(file.getUuid())
                        .build());

        itemRepository.deleteById(itemId);
    }

    @Transactional
    public ItemResponse updateMetadata(Long itemId, ItemUpdateDto dto) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ItemEntity file =
                itemRepository
                        .findByIdAndOwnerId(itemId, owner.getId())
                        .orElseThrow(() -> new BusinessException("File not exist"));

        Item domain = itemMapper.toDomain(file);

        Item update = itemMapper.update(domain, dto);

        ItemEntity entity = itemMapper.toEntity(update);

        ItemEntity save = itemRepository.save(entity);

        return itemMapper.toResponse(save);
    }

    @Transactional
    public List<ItemResponse> findItemsMetadataByQuery(SearchMetadataQuery searchMetadataQuery) {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<ItemEntity> items = itemQuery.findItemsByParameters(searchMetadataQuery, owner);

        return items.stream()
                .map(itemMapper::toResponse)
                .sorted(Comparator.comparing(ItemResponse::getId))
                .collect(Collectors.toList());
    }

    public List<StorageResponse> getAllStorages() {
        Owner owner = (Owner) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var entity = storageRepository.findAllByOwnerId(owner.getId());

        return entity.stream()
                .map(storageMapper::toResponse)
                .sorted(Comparator.comparing(StorageResponse::getId))
                .collect(Collectors.toList());
    }
}
