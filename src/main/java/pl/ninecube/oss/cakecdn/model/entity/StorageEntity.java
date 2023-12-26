/* (C)2023 */
package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "storages")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class StorageEntity extends BaseEntity {

  @Id
  @SequenceGenerator(
          name = "storages_seq_generator",
          sequenceName = "storages_seq",
          allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storages_seq_generator")
  protected Long id;

  String name;

  @ManyToOne
  @JoinColumn(name = "project_id")
  ProjectEntity project;
}
