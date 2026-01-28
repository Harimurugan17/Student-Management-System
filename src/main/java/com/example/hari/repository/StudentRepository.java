package com.example.hari.repository;

import com.example.hari.model.Student;
import com.example.hari.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

