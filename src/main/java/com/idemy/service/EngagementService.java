package com.idemy.service;

import com.idemy.dao.entity.*;
import com.idemy.dao.repository.*;
import com.idemy.dto.request.ReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EngagementService {

    private final ReviewRepository reviewRepository;
    private final UserProgressRepository progressRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    // RƏY YAZMAQ
    @Transactional
    public String addReview(Long courseId, ReviewRequest request) {
        User user = getCurrentUser();
        
        // 1. Kursu alıbmı?
        if (!enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), courseId)) {
            throw new RuntimeException("Rəy yazmaq üçün kursu satın almalısınız!");
        }

        // 2. Əvvəl rəy yazıbmı?
        if (reviewRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new RuntimeException("Siz artıq bu kursa rəy yazmısınız!");
        }

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUser(user);
        review.setCourse(new Course(courseId)); // Sadəcə ID ilə bağlayırıq

        reviewRepository.save(review);
        return "Rəyiniz uğurla əlavə edildi!";
    }

    // DƏRSİ BİTMİŞ KİMİ İŞARƏLƏMƏK
    @Transactional
    public String completeLesson(Long lessonId) {
        User user = getCurrentUser();
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        Long courseId = lesson.getSection().getCourse().getId();

        // Kursu alıbmı?
        if (!enrollmentRepository.existsByStudentIdAndCourseId(user.getId(), courseId)) {
            throw new RuntimeException("Bu kursu satın almamısınız!");
        }

        // Artıq bitiribmi?
        if (progressRepository.existsByUserIdAndLessonId(user.getId(), lessonId)) {
            return "Dərs artıq bitirilib.";
        }

        UserProgress progress = new UserProgress();
        progress.setUser(user);
        progress.setLesson(lesson);
        progress.setCompletedAt(LocalDateTime.now());

        progressRepository.save(progress);
        return "Dərs bitmiş kimi işarələndi!";
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}