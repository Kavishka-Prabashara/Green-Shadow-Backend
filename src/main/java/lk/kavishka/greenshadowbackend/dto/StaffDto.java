package lk.kavishka.greenshadowbackend.dto;

import lk.kavishka.greenshadowbackend.entity.Gender;
import lk.kavishka.greenshadowbackend.entity.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffDto {
    private String id;
    private String staffCode;
    private String firstName;
    private String lastName;
    private String designation;
    private Gender gender;
    private LocalDate joinedDate;
    private LocalDate dob;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    private String addressLine5;
    private String contactNo;
    private String email;
    private Role role;
    // associations
    private Set<String> fieldIds;
    private Set<String> vehicleIds;
}
