package com.example.hari;

import com.example.hari.model.User;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenNameHasSpecialCharacters_thenValidationFails() {
        User user = new User();
        user.setName("User@123");
        user.setPassword("password");
        user.setEmail("test@example.com");

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Should fail validation for special characters in name");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name must not contain special characters")));
    }

    @Test
    public void whenPasswordIsTooShort_thenValidationFails() {
        User user = new User();
        user.setName("Valid Name");
        user.setPassword("123");
        user.setEmail("test@example.com");

        var violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Should fail validation for short password");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password must be at least 5 characters long")));
    }

    @Test
    public void whenInputIsValid_thenValidationSucceeds() {
        User user = new User();
        user.setName("Valid Name");
        user.setPassword("password123");
        user.setEmail("test@example.com");

        var violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Should pass validation for valid input");
    }
}
