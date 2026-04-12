package com.idemy.dao.repository;

import com.idemy.dao.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    // Kursun tələbə sayını dinamik hesablamaq üçün
    Long countByCourseId(Long courseId);
}