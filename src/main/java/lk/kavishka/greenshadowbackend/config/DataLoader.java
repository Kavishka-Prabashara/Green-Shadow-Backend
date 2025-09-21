package lk.kavishka.greenshadowbackend.config;

import lk.kavishka.greenshadowbackend.entity.User;
import lk.kavishka.greenshadowbackend.entity.Role;
import lk.kavishka.greenshadowbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin@greenshadow.com")) {
            userRepository.save(User.builder()
                    .email("admin@greenshadow.com")
                    .password(passwordEncoder.encode("Admin@123"))
                    .role(Role.MANAGER)
                    .build());
        }
    }
}
