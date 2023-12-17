package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "accounts")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class AccountEntity extends BaseEntity {

    @Id
    @SequenceGenerator(name = "accounts_seq_generator", sequenceName = "projects_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accounts_seq_generator")
    protected Long id;

    private String username;
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<ProjectEntity> projects;
}
