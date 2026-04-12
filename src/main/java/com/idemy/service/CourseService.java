package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.User;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dao.repository.EnrollmentRepository;
import com.idemy.dao.repository.UserRepository;
import com.idemy.dto.request.CourseCreateRequest;
import com.idemy.dto.responce.CourseResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;

    public CourseResponse createCourse(CourseCreateRequest request) {
        // 1. Token-dən gələn email vasitəsilə müəllimi tapırıq
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User instructor = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Müəllim tapılmadı"));

        // 2. Entity yarat və məlumatları doldur
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setInstructor(instructor);

        // 3. Bazaya yaz
        Course savedCourse = courseRepository.save(course);

        return mapToResponse(savedCourse);/// BUUNA BAXARSAN NIYE GERI QAYTARIRIQ
    }

    public List<CourseResponse> getMyCourses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return courseRepository.findByInstructorEmail(email)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Transactional
    public CourseResponse updateCourse(Long id, CourseCreateRequest request) {
        // 1. Kursu tap
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

        // 2. Təhlükəsizlik yoxlaması: Bu kursu redaktə edən şəxs onun müəllimidir?
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!course.getInstructor().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Sizin bu kursu redaktə etmək icazəniz yoxdur!");
        }

        // 3. Məlumatları yenilə
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());

        // 4. Yadda saxla və cavabı qaytar
        return mapToResponse(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        // 1. Kursu tap
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

        // 2. Təhlükəsizlik yoxlaması
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!course.getInstructor().getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Sizin bu kursu silmək icazəniz yoxdur!");
        }

        // 3. Sil
        courseRepository.delete(course);
    }

    // Entity-ni DTO-ya çevirən köməkçi metod
    private CourseResponse mapToResponse(Course course) {
        long studentCount = enrollmentRepository.countByCourseId(course.getId());
        
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .instructorName(course.getInstructor().getFullName())
                .studentCount(studentCount)
                .build();
    }
}