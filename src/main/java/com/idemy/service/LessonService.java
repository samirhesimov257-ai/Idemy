package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.Lesson;
import com.idemy.dao.entity.User;
import com.idemy.dao.repository.EnrollmentRepository;
import com.idemy.dao.repository.LessonRepository;
import com.idemy.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public Resource getLessonVideoResource(Long lessonId) {
        // 1. Cari dərsi tap
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        // 2. Cari kursu tap (zəncirvari)
        Course course = lesson.getSection().getCourse();

        // 3. Cari istifadəçini tap
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Əgər istifadəçi anonimdirsə (permitAll olsa belə), email "anonymousUser" gələcək.
        if (email.equals("anonymousUser")) {
            throw new RuntimeException("Videonu izləmək üçün daxil olmalısınız!");
        }

        User user = userRepository.findByEmail(email).orElseThrow();

        // 4. ALIB-ALMADIĞINI YOXLA (Əsas müdafiə)
        boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), course.getId());
        boolean isInstructor = course.getInstructor().getEmail().equals(email);

        if (isEnrolled || isInstructor) {
            return fileService.loadFileAsResource(lesson.getVideoUrl());
        } else {
            throw new RuntimeException("Bu videonu izləmək üçün kursu satın almalısınız!");
        }

    }
}