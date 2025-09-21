package lk.kavishka.greenshadowbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldDto {
    private String fieldCode;
    private String name;
    private String location;
    private Double extentSqm;
    private String image1;
    private String image2;
}
