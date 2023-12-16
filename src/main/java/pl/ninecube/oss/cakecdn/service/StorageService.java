package pl.ninecube.oss.cakecdn.service;

import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ninecube.oss.cakecdn.exception.BusinessException;
import pl.ninecube.oss.cakecdn.exception.TechnicalException;
import pl.ninecube.oss.cakecdn.model.domain.Bucket;
import pl.ninecube.oss.cakecdn.model.domain.Storage;
import pl.ninecube.oss.cakecdn.model.dto.BucketCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.BucketResponse;
import pl.ninecube.oss.cakecdn.model.dto.StorageCreateDto;
import pl.ninecube.oss.cakecdn.model.dto.StorageResponse;
import pl.ninecube.oss.cakecdn.model.entity.BucketEntity;
import pl.ninecube.oss.cakecdn.model.mapper.BucketMapper;
import pl.ninecube.oss.cakecdn.repository.BucketRepository;
import pl.ninecube.oss.cakecdn.repository.StorageRepository;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final MinioClient client;
    private final BucketRepository bucketRepository;
    private final StorageRepository storageRepository;
    private final BucketMapper bucketMapper;

    @SneakyThrows
    public BucketResponse createBucket(BucketCreateDto dto) {
        if (isBucketExist(dto.getName())) {
            BucketEntity bucketEntity = bucketRepository.findByName(dto.getName())
                    .orElseThrow(() -> new TechnicalException("Bucket not found"));

            return bucketMapper.toResponse(bucketEntity);
        }

        Bucket bucket = Bucket.builder()
                .name(dto.getName())
                .build();

        client.makeBucket(MakeBucketArgs.builder()
                .bucket(bucket.getName())
                .build());

        BucketEntity bucketEntity = bucketMapper.toEntity(bucket);

        bucketRepository.save(bucketEntity);

        return bucketMapper.toResponse(bucketEntity);
    }

    private boolean isBucketExist(String name) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        return client.bucketExists(BucketExistsArgs.builder()
                .bucket(name)
                .build());
    }

    public BucketResponse getBucketMetadata(Long bucketId) {
        BucketEntity bucketEntity = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BusinessException("Bucket not exist"));

        return bucketMapper.toResponse(bucketEntity);
    }

    @SneakyThrows
    public void deleteBucket(Long bucketId) {
        BucketEntity bucketEntity = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BusinessException("Bucket not exist"));

        if (isBucketExist(bucketEntity.getName())) {
            client.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketEntity.getName())
                    .build());
        }

        bucketRepository.delete(bucketEntity);
    }

    @SneakyThrows
    public StorageResponse saveFile(Long bucketId, StorageCreateDto dto, MultipartFile file) {
        var bucketEntity = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BusinessException("Bucket not exist"));

        var bucket = bucketMapper.toDomain(bucketEntity);

        var storage = Storage.builder()
                .fileName(dto.getFileName())
                .originalFilename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .uuid(UUID.randomUUID().toString())
                .tags(dto.getTags())
                .categories(dto.getCategories())
                .parameters(dto.getParameters())
                .build();

        bucket.getFiles().add(storage);

        if (isBucketExist(bucket.getName())) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(storage.getUuid())
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        }

        return null;
    }

    public byte[] getFile(Long storageId) {
        return null;
    }

    public StorageResponse getFileMetadata(Long storageId) {
        return null;
    }

    public void deleteFile(Long storageId) {

    }
}
