package lk.kavishka.greenshadowbackend.service;

import lk.kavishka.greenshadowbackend.dto.AuthRequest;
import lk.kavishka.greenshadowbackend.dto.AuthResponse;
import lk.kavishka.greenshadowbackend.dto.UserDto;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    void register(UserDto dto);
}
