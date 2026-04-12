package com.idemy.dao.repository;

import com.idemy.dao.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Müəllimin öz kurslarını siyahılamaq üçün
    List<Course> findByInstructorEmail(String email);
}