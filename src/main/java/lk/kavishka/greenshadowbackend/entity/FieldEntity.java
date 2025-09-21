package lk.kavishka.greenshadowbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String fieldCode;

    private String name;

    // store as "lat,lng" string or use Point type with spatial extension
    private String location;

    private Double extentSqm;

    @Lob
    private String image1;

    @Lob
    private String image2;

    @ManyToMany
    @JoinTable(name = "field_staff",
            joinColumns = @JoinColumn(name = "field_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id"))
    private Set<Staff> staff = new HashSet<>();

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL)
    private Set<Crop> crops = new HashSet<>();
}
