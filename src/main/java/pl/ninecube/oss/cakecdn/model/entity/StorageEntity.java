package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Builder
@Table(name = "storages")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class StorageEntity extends BaseEntity {

    @Id
    @SequenceGenerator(name = "storages_seq_generator", sequenceName = "storages_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storages_seq_generator")
    protected Long id;

    String name;

    @ManyToOne
    @JoinColumn(name = "project_id")
    ProjectEntity project;

//    @OneToMany(mappedBy = "storage")
//    Set<ItemEntity> files;
}
