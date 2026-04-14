package com.idemy.dao.repository;

import com.idemy.dao.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Kursun tələbə sayını dinamik hesablamaq üçün
    Long countByCourseId(Long courseId);

    // Tələbə bu kursu artıq alıb?
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    // Tələbənin bütün aldığı kurslar
    List<Enrollment> findByStudentId(Long studentId);
}