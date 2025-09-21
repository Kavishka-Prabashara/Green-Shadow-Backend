package lk.kavishka.greenshadowbackend.dto;

import lk.kavishka.greenshadowbackend.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    private String password;
    private Role role;
}
