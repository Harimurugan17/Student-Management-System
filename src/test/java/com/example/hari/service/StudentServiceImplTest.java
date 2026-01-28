package com.example.hari.service;

import com.example.hari.model.Student;
import com.example.hari.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @Test
    public void testGetAllStudents() {
        Student s1 = new Student();
        s1.setName("S1");
        Student s2 = new Student();
        s2.setName("S2");

        when(repository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Student> students = service.getAll();
        assertEquals(2, students.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testSaveStudent() {
        Student student = new Student();
        student.setName("Save Me");

        service.save(student);
        verify(repository, times(1)).save(student);
    }

    @Test
    public void testGetStudent() {
        Student student = new Student();
        student.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(student));

        Student found = service.get(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
    }


    @Test
    public void testDeleteStudent() {
        doNothing().when(repository).deleteById(1L);
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
