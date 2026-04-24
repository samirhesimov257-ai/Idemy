package com.idemy.dao.repository;

import com.idemy.dao.entity.Course;
import com.idemy.dto.responce.CourseResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Müəllimin öz kurslarını siyahılamaq üçün
    List<Course> findByInstructorEmail(String email);

    // Kursun adına görə axtarış (Böyük-kiçik hərf fərqi olmadan)
    List<Course> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT c FROM Course c WHERE LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Course> searchByKeyword(@Param("keyword") String keyword);

    List<Course> findByInstructorFullNameContainingIgnoreCase(String fullName);

    // Müəyyən qiymət aralığında kurslar
    List<CourseResponse> findByPriceBetween(BigDecimal min, BigDecimal max);

}