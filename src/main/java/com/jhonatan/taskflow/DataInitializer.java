package com.jhonatan.taskflow;

import com.jhonatan.taskflow.domain.entity.Role;
import com.jhonatan.taskflow.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> roles = List.of(
                "ADMIN",
                "PROJECT_MANAGER",
                "DEVELOPER"
        );

        roles.forEach(roleName -> {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(
                            Role.builder().name(roleName).build()
                    ));
        });
    }
}
