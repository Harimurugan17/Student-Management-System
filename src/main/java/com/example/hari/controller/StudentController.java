package com.example.hari.controller;

import com.example.hari.model.Student;
import com.example.hari.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private com.example.hari.repository.StudentFileRepository fileRepo;
    
    @Autowired
    private com.example.hari.service.FileStorageService fileStorageService;

    @GetMapping("/")
    public String view(Model model) {
        model.addAttribute("students", service.getAll());
        return "students";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("student", new Student());
        return "add-student";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Student student = service.get(id); 
        model.addAttribute("student", student);
        return "add-student";
    }

    @PostMapping("/save")
    public String save(@jakarta.validation.Valid @ModelAttribute Student student, org.springframework.validation.BindingResult result, @org.springframework.web.bind.annotation.RequestParam("studentImage") org.springframework.web.multipart.MultipartFile file) {
        System.out.println("DEBUG: Entering save method");
        if (result.hasErrors()) {
            System.out.println("DEBUG: Validation errors found: " + result.getAllErrors());
            return "add-student";
        }
        try {
            System.out.println("DEBUG: File received. Empty: " + file.isEmpty() + ", Name: " + file.getOriginalFilename() + ", Size: " + file.getSize());
            if (!file.isEmpty()) {
                // Store file to filesystem
                String fileName = fileStorageService.storeFile(file);
                
                com.example.hari.model.StudentFile studentFile = new com.example.hari.model.StudentFile();
                studentFile.setFileName(file.getOriginalFilename()); // Original Name
                studentFile.setFilePath(fileName); // Stored Name (UUID + Original)
                studentFile.setFileType(file.getContentType());
                
                // Explicitly save the file metadata first
                studentFile = fileRepo.save(studentFile);
                System.out.println("DEBUG: StudentFile metadata saved. ID: " + studentFile.getId());
                
                student.setFile(studentFile);
                
                System.out.println("DEBUG: File processed and saved to location: " + fileName);
            } else {
                System.out.println("DEBUG: File is empty!");
            }

            org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            System.out.println("DEBUG: Current User: " + authentication.getName());
    
            service.save(student);
            System.out.println("DEBUG: Student saved. ID: " + student.getId());
        
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG: Exception occurred: " + e.getMessage());
            // You might want to pass error to model to show on UI
            return "add-student";
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }

    @GetMapping("/download/{id}")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> downloadFile(@PathVariable Long id) {
        Student student = service.get(id);
        if (student != null && student.getFile() != null) {
            com.example.hari.model.StudentFile fileMetadata = student.getFile();
            
            // Load file as Resource
            org.springframework.core.io.Resource resource = fileStorageService.loadFileAsResource(fileMetadata.getFilePath());

            return org.springframework.http.ResponseEntity.ok()
                    .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getFileName() + "\"")
                    .body(resource);
        }
        return org.springframework.http.ResponseEntity.notFound().build();
    }
}