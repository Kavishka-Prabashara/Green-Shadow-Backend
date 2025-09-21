package lk.kavishka.greenshadowbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentDto {
    private String id;
    private String equipmentCode;
    private String name;
    private String type;
    private String status;
    private String assignedStaffId;
    private String assignedFieldId;
}
