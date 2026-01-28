package com.example.hari;

import com.example.hari.model.User;
import com.example.hari.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class StudentAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository repository, PasswordEncoder passwordEncoder) {
        return (args) -> {


            String testEmail = "admin@example.com";
            if (repository.findFirstByEmail(testEmail) == null) {
                User user = new User();
                user.setName("Admin User");
                user.setEmail(testEmail);
                user.setPassword(passwordEncoder.encode("password"));
                repository.save(user);
                System.out.println("Created default user: " + testEmail + " / password");
            } else {
                 System.out.println("Default user " + testEmail + " already exists.");
            }
            System.out.println("--------------------------------------");
        };
    }
}
