package lk.kavishka.greenshadowbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String equipmentCode;
    private String name;
    private String type; // enum optional
    private String status;

    @ManyToOne
    private Staff assignedStaff;

    @ManyToOne
    private FieldEntity assignedField;
}
