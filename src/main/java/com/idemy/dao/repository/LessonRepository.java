package com.idemy.dao.repository;

import com.idemy.dao.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson,Long> {
    long countBySectionId(Long sectionId);
}
