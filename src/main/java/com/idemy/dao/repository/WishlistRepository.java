package com.idemy.dao.repository;

import com.idemy.dao.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);
    List<WishlistItem> findByUserId(Long userId);
    Optional<WishlistItem> findByUserIdAndCourseId(Long userId, Long courseId);
}
