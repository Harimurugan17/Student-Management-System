package com.example.hari.controller;

import com.example.hari.model.Student;
import com.example.hari.service.StudentService;
import com.example.hari.repository.StudentFileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.hari.security.JwtAuthenticationFilter;
import com.example.hari.security.JwtUtil;
import com.example.hari.security.CustomUserDetailsService;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.mock.web.MockMultipartFile;

@WebMvcTest(StudentController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@Import(JwtAuthenticationFilter.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService service;

    @MockBean
    private StudentFileRepository fileRepo;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private com.example.hari.service.FileStorageService fileStorageService;

    @MockBean
    private com.example.hari.repository.UserRepository userRepository;

    @MockBean
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Test
    public void testViewHomePage() throws Exception {
        Student s1 = new Student();
        s1.setName("Hari");
        given(service.getAll()).willReturn(Arrays.asList(s1));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"));
    }

    @Test
    public void testAddStudentPage() throws Exception {
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-student"))
                .andExpect(model().attributeExists("student"));
    }

    @Test
    public void testEditStudentPage() throws Exception {
        Student student = new Student();
        student.setId(1L);
        given(service.get(1L)).willReturn(student);

        mockMvc.perform(get("/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-student"))
                .andExpect(model().attributeExists("student"));
    }
    
    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(get("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testSaveStudentWithFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "studentImage", 
                "test.txt", 
                "text/plain", 
                "Hello, World!".getBytes()
        );
        
        given(fileStorageService.storeFile(any())).willReturn("uuid_test.txt");
        given(fileRepo.save(any())).willAnswer(invocation -> {
            com.example.hari.model.StudentFile savedFile = invocation.getArgument(0);
            savedFile.setId(10L);
            return savedFile;
        });

        mockMvc.perform(multipart("/save")
                .file(file)
                .param("name", "New Student")
                .param("email", "new@example.com")
                .param("course", "New Course")
                .with(csrf())) // CSRF token is required for POST
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(fileStorageService).storeFile(any());
        verify(service).save(any(Student.class));
    }
}
