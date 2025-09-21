package lk.kavishka.greenshadowbackend.service.impl;


import lk.kavishka.greenshadowbackend.dto.AuthRequest;
import lk.kavishka.greenshadowbackend.dto.AuthResponse;
import lk.kavishka.greenshadowbackend.dto.UserDto;
import lk.kavishka.greenshadowbackend.entity.User;
import lk.kavishka.greenshadowbackend.repository.UserRepository;
import lk.kavishka.greenshadowbackend.security.JwtUtils;
import lk.kavishka.greenshadowbackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse login(AuthRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User principal = (User) auth.getPrincipal();
        String token = jwtUtils.generateToken(principal.getEmail());
        return new AuthResponse(token);
    }

    @Override
    public void register(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
        userRepository.save(user);
    }
}
