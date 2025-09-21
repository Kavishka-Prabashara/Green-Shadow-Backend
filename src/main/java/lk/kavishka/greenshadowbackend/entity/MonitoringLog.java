package lk.kavishka.greenshadowbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "monitoring_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String logCode;
    private LocalDate logDate;

    @Lob
    private String details;

    @Lob
    private String observedImage;

    @ManyToMany
    @JoinTable(name = "log_fields",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "field_id"))
    private Set<FieldEntity> fields = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "log_crops",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "crop_id"))
    private Set<Crop> crops = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "log_staff",
            joinColumns = @JoinColumn(name = "log_id"),
            inverseJoinColumns = @JoinColumn(name = "staff_id"))
    private Set<Staff> staff = new HashSet<>();
}
