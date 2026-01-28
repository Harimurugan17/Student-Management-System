package com.example.hari;

import com.example.hari.model.User;
import com.example.hari.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class LoginTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Test
    public void loginWithEmail_shouldSucceed() throws Exception {
        // Setup user
        User user = new User();
        user.setEmail("testlogin@example.com");
        user.setPassword("password123");
        user.setName("Test User");
        userService.register(user);

        // Attempt login with "email" parameter
        mockMvc.perform(post("/login")
                .param("email", "testlogin@example.com")
                .param("password", "password123"))
                .andExpect(status().isFound()) // 302 Redirect
                .andExpect(redirectedUrl("/")); 
    }
    
    @Test
    public void loginWithWrongPassword_shouldFail() throws Exception {
         // Setup user
        User user = new User();
        user.setEmail("testfail@example.com");
        user.setPassword("password123");
        user.setName("Test User");
        userService.register(user);
        
        mockMvc.perform(post("/login")
                .param("email", "testfail@example.com")
                .param("password", "wrong"))
                .andExpect(status().isFound()) // 302 Redirect
                .andExpect(redirectedUrl("/login?error"));
    }
}
