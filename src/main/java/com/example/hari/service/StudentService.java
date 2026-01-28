package com.example.hari.service;

import com.example.hari.model.Student;
import com.example.hari.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repo;

    public List<Student> getAll() {
        return repo.findAll();
    }

    public void save(Student s) {
        repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Student get(Long id) {
        return repo.findById(id).get();
    }
}

