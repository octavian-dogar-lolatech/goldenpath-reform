package com.lola.goldenpath;

import com.lola.goldenpath.model.entity.RoleEntity;
import com.lola.goldenpath.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GoldenPathApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoldenPathApplication.class, args);
    }

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            roleRepository.save(RoleEntity.builder().name("ROLE_USER").build());
            roleRepository.save(RoleEntity.builder().name("ROLE_ADMIN").build());
        };
    }
}
