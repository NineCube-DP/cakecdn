/* (C)2024 */
package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "applications_tokens")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class ApplicationTokenEntity extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "applications_tokens_seq_generator",
            sequenceName = "applications_tokens_seq",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "applications_tokens_seq_generator")
    protected Long id;

    private String applicationName;

    private String token;

    private Long projectId;
}
