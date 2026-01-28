package com.example.hari.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StudentValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testStudentValidation() {
        Student student = new Student();
        student.setName("Hari");
        student.setEmail("hari@example.com");
        student.setCourse("Java");

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testStudentValidationFailure() {
        Student student = new Student();
        student.setName(""); // Invalid: NotBlank
        student.setEmail("invalid-email"); // Invalid: Email
        student.setCourse(""); // Invalid: NotBlank

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertFalse(violations.isEmpty());
        // We expect 3 violations
        assertEquals(3, violations.size());
    }
}
