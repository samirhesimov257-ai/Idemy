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
        // 1. Dərsi tap
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Dərs tapılmadı"));

        // 2. Cari istifadəçini tap
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("İstifadəçi tapılmadı"));

        // 3. İcazə yoxlanışı (Paywall Logic)
        // A) İstifadəçi bu kursun müəllimidir?
        boolean isInstructor = lesson.getSection().getCourse().getInstructor().getEmail().equals(email);
        
        // B) İstifadəçi bu kursu satın alıb?
        boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), lesson.getSection().getCourse().getId());

        if (isInstructor || isEnrolled) {
            // İcazə var -> Videonu gətir
            return fileService.loadFileAsResource(lesson.getVideoUrl());
        } else {
            // İcazə yoxdur
            throw new RuntimeException("Bu videonu izləmək üçün kursu satın almalısınız!");
        }
    }
    public Resource getLessonVideo(Long lessonId) {
        // 1. Tələbənin izləmək istədiyi dərsi tapırıq
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Dərs tapılmadı"));

        // 2. Dərsin aid olduğu KURS-u tapırıq (Zəncir: Lesson -> Section -> Course)
        // Sənin entity-də line 34-də 'section' var.
        Course course = lesson.getSection().getCourse();

        // 3. İndi kursun ID-si əlimizdədir. Alışı yoxlaya bilərik.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email).orElseThrow();

        boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(
                currentUser.getId(),
                course.getId() // Bax, burada məhz KURSU alıb-almadığını yoxlayırıq
        );

        if (isEnrolled || course.getInstructor().getEmail().equals(email)) {
            return fileService.loadFileAsResource(lesson.getVideoUrl());
        } else {
            throw new RuntimeException("Bu kursu satın almamısınız!");
        }
    }
}