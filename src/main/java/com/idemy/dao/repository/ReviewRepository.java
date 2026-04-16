package com.idemy.dao.repository;

import com.idemy.dao.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
}
