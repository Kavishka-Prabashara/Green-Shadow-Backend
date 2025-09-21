package lk.kavishka.greenshadowbackend.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonitoringLogDto {
    private String id;
    private String logCode;
    private LocalDate logDate;
    private String details;
    private String observedImage;
    private Set<String> fieldIds;
    private Set<String> cropIds;
    private Set<String> staffIds;
}
