package lk.kavishka.greenshadowbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "crops")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String cropCode;
    private String commonName;
    private String scientificName;
    @Lob
    private String image;
    private String category;
    private String season;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private FieldEntity field;
}
