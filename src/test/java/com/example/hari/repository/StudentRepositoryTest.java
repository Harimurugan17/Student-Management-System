package com.example.hari.repository;

import com.example.hari.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentRepositoryTest {

    @org.springframework.boot.test.mock.mockito.MockBean
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepository repository;

    @Test
    public void testSaveAndFindStudent() {
        Student student = new Student();
        student.setName("Test Student");
        student.setEmail("test@example.com");
        student.setCourse("Test Course");

        Student savedStudent = repository.save(student);
        assertNotNull(savedStudent.getId());

        Optional<Student> foundStudent = repository.findById(savedStudent.getId());
        assertTrue(foundStudent.isPresent());
        assertEquals("Test Student", foundStudent.get().getName());
    }

    @Test
    public void testDeleteStudent() {
        Student student = new Student();
        student.setName("Delete Me");
        student.setEmail("delete@example.com");
        student.setCourse("Delete Course");
        Student savedStudent = repository.save(student);

        repository.deleteById(savedStudent.getId());

        Optional<Student> foundStudent = repository.findById(savedStudent.getId());
        assertFalse(foundStudent.isPresent());
    }
}
