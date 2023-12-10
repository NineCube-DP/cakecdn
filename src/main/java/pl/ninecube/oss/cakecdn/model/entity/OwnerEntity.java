package pl.ninecube.oss.cakecdn.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Table(name = "owners")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
public class OwnerEntity {
    @Id
    @SequenceGenerator(name = "owner_seq_generator", sequenceName = "owners_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "owner_seq_generator")
    private Long id;

    private String username;
    private String password;
}
