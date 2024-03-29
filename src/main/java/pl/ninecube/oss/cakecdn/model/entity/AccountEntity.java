/* (C)2023-2024 */
package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@Table(name = "accounts")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class AccountEntity {

    @Id
    @SequenceGenerator(
            name = "accounts_seq_generator",
            sequenceName = "projects_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq_generator")
    protected Long id;

    private String role;

    private Boolean fullAccessPermission;

    private String username;
    private String password;

    @Version
    @Builder.Default
    private Long version = 1L;
}
