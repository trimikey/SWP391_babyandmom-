package com.swp.BabyandMom.Entity;
import com.swp.BabyandMom.Entity.Enum.RoleType;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleType name;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
