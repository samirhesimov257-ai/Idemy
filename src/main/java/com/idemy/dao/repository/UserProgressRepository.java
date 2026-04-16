package com.idemy.dao.repository;

import com.idemy.dao.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    boolean existsByUserIdAndLessonId(Long userId, Long lessonId);
    long countByUserIdAndLesson_Section_Course_Id(Long userId, Long courseId);
}
