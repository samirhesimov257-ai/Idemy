package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.User;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dao.repository.EnrollmentRepository;
import com.idemy.dao.repository.UserRepository;
import com.idemy.dto.request.CourseCreateRequest;
import com.idemy.dto.responce.CourseResponse;
import com.idemy.mapper.CourseMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private static final String ALL_COURSES_CACHE = "allCourses";
    private static final String INSTRUCTOR_COURSES_CACHE = "coursesByInstructor";

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private  final CourseMapper courseMapper ;

    @CacheEvict(cacheNames = {ALL_COURSES_CACHE, INSTRUCTOR_COURSES_CACHE}, allEntries = true)
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

        return courseMapper.toDto(savedCourse);/// BUUNA BAXARSAN NIYE GERI QAYTARIRIQ
    }

    public List<CourseResponse> getMyCourses() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return courseMapper.listToDtoList(courseRepository.findByInstructorEmail(email));

    }

    @Cacheable(cacheNames = ALL_COURSES_CACHE)
    public List<CourseResponse> getAllCourses() {
        return courseMapper.listToDtoList(courseRepository.findAll());
    }

    @Cacheable(cacheNames = INSTRUCTOR_COURSES_CACHE, key = "#name.toLowerCase()")
    public List<CourseResponse> searchByInstructor(String name) {
        return courseMapper.listToDtoList(courseRepository.findByInstructorFullNameContainingIgnoreCase(name));
    }

    @Transactional
    @CacheEvict(cacheNames = {ALL_COURSES_CACHE, INSTRUCTOR_COURSES_CACHE}, allEntries = true)
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
        return courseMapper.toDto(courseRepository.save(course));
    }

    @CacheEvict(cacheNames = {ALL_COURSES_CACHE, INSTRUCTOR_COURSES_CACHE}, allEntries = true)
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
}