package lk.kavishka.greenshadowbackend.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropDto {
    private String id;
    private String cropCode;
    private String commonName;
    private String scientificName;
    private String image;
    private String category;
    private String season;
    // link to parent field
    private String fieldId;
}
