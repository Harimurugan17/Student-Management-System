package com.example.hari.service;

import com.example.hari.model.User;
import com.example.hari.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImpTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;


    @Test
    public void testRegisterUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        service.register(user);

        verify(passwordEncoder, times(1)).encode("password");
        verify(repository, times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(repository.findFirstByEmail("test@example.com")).thenReturn(user);

        User found = service.findByEmail("test@example.com");
        
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
        verify(repository, times(1)).findFirstByEmail("test@example.com");
    }
}
