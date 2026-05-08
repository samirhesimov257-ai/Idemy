package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.User;
import com.idemy.dao.entity.WishlistItem;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dao.repository.UserRepository;
import com.idemy.dao.repository.WishlistRepository;
import com.idemy.dto.responce.CourseResponse;
import com.idemy.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public String addToWishlist(Long courseId) {
        User user = getCurrentUser();
        if (wishlistRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            return "Kurs artıq wishlist-dədir.";
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));
        WishlistItem item = new WishlistItem();
        item.setUser(user);
        item.setCourse(course);
        wishlistRepository.save(item);
        return "Kurs wishlist-ə əlavə edildi.";
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getWishlist() {
        User user = getCurrentUser();
        return wishlistRepository.findByUserId(user.getId())
                .stream()
                .map(WishlistItem::getCourse)
                .map(courseMapper::toDto)
                .toList();
    }

    @Transactional
    public String removeFromWishlist(Long courseId) {
        User user = getCurrentUser();
        WishlistItem item = wishlistRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new RuntimeException("Wishlist-də bu kurs tapılmadı"));
        wishlistRepository.delete(item);
        return "Kurs wishlist-dən silindi.";
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }
}
