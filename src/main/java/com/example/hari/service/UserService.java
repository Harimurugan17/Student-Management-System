package com.example.hari.service;

import com.example.hari.model.User;
import com.example.hari.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repo.save(user);
    }
    
    // Kept for backward compatibility if needed, but not used for JWT login
    public User findByEmail(String email) {
        return repo.findFirstByEmail(email);
    }
}
