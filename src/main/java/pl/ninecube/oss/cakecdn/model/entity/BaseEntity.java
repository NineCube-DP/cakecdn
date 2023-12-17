package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@SuperBuilder
@MappedSuperclass
@EqualsAndHashCode
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Version
    @Builder.Default
    private Long version = 1L;

    @CreatedDate
    private OffsetDateTime creationTimeStamp;

    @LastModifiedDate
    private OffsetDateTime modificationTimeStamp;

}
