package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "projects")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class ProjectEntity {
    @Id
    @SequenceGenerator(name = "projects_seq_generator", sequenceName = "projects_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "projects_seq_generator")
    private Long id;

    private String name;
    private String baseUrl;

    private Boolean enabled = false;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity owner;
}
