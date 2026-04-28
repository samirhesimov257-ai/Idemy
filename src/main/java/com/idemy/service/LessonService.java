package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.Lesson;
import com.idemy.dao.entity.User;
import com.idemy.dao.repository.EnrollmentRepository;
import com.idemy.dao.repository.LessonRepository;
import com.idemy.dao.repository.UserRepository;
import com.idemy.exception.VideoAccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    public InputStream getLessonVideoStream(Long lessonId) {
        // 1. Cari dərsi tap
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();

        // 2. Cari kursu tap (zəncirvari)
        Course course = lesson.getSection().getCourse();

        // 3. Cari istifadəçini tap
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // Əgər istifadəçi anonimdirsə (permitAll olsa belə), email "anonymousUser" gələcək.
        if (email.equals("anonymousUser")) {
            throw new VideoAccessDeniedException("Videonu izləmək üçün daxil olmalısınız!");
        }

        return getAuthorizedStream(lesson, course, email);
    }

    private InputStream getAuthorizedStream(Lesson lesson, Course course, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        // 4. ALIB-ALMADIĞINI YOXLA (Əsas müdafiə)
        boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), course.getId());
        boolean isInstructor = course.getInstructor().getEmail().equals(email);

        if (isEnrolled || isInstructor) {
            return fileService.loadFileAsStream(lesson.getVideoUrl());
        } else {
            throw new VideoAccessDeniedException("Bu videonu izləmək üçün kursu satın almalısınız!");
        }
    }
}