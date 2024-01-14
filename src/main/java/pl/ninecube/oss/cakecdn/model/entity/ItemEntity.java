/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@SuperBuilder
@Table(name = "items")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class ItemEntity extends BaseEntity {

    @Id
    @SequenceGenerator(name = "items_seq_generator", sequenceName = "items_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq_generator")
    Long id;

    String fileName;
    String originalFileName;
    String contentType;
    Long fileSize;
    String uuid;
    String url;

    Long storageId;

    //  @ManyToOne
    //  @JoinColumn(name = "storage_id")
    //  StorageEntity storage;

    @Builder.Default
    @Column(name = "tag", nullable = false)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "item_tags", joinColumns = @JoinColumn(name = "item_id"))
    Set<String> tags = new HashSet<>();

    @Builder.Default
    @Column(name = "category", nullable = false)
    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(name = "item_categories", joinColumns = @JoinColumn(name = "item_id"))
    Set<String> categories = new HashSet<>();

    @Builder.Default
    @Column(name = "value")
    @MapKeyColumn(name = "name")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "item_parameters", joinColumns = @JoinColumn(name = "item_id"))
    Map<String, String> parameters = new HashMap<>();
}
