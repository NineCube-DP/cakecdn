/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@MappedSuperclass
@EqualsAndHashCode
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Version
    protected Long version = 1L;

    @CreatedDate
    private OffsetDateTime creationTimeStamp;

    @LastModifiedDate
    private OffsetDateTime modificationTimeStamp;

    protected BaseEntity(BaseEntityBuilder<?, ?> b) {
        this.version = b.version;
    }

    public static abstract class BaseEntityBuilder<C extends BaseEntity, B extends BaseEntityBuilder<C, B>> {
        private Long version;

        public B version(Long version) {
            this.version = version;
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "BaseEntity.BaseEntityBuilder(version=" + this.version + ")";
        }
    }
}
