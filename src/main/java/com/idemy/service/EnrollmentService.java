package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.Enrollment;
import com.idemy.dao.entity.User;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dao.repository.EnrollmentRepository;
import com.idemy.dao.repository.UserRepository;
import com.idemy.dto.NotificationDTO;
import com.idemy.service.messaging.EnrollmentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentProducer enrollmentProducer;

    @Transactional
    public String enrollInCourse(Long courseId) {
        // 1. Giriş edən tələbəni tap
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        // 2. Kursu tap
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));

        // 3. Yoxla: Artıq alıbmı?
        if (enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), courseId)) {
            throw new RuntimeException("Siz artıq bu kursa qeydiyyatdan keçmisiniz!");
        }

        // 4. Qeydiyyatı yarat (Enrollment)
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(user);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());

        enrollmentRepository.save(enrollment);

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .email(user.getEmail())
                .courseName(course.getTitle())
                .build();
        enrollmentProducer.sendEnrollmentNotification(notificationDTO);

        return "Kurs uğurla alındı!";
    }
}