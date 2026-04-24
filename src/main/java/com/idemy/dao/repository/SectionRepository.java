package com.idemy.dao.repository;

import com.idemy.dao.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section,Long> {
    long countByCourseId(Long courseId);
    boolean existsByCourseIdAndOrderIndex(Long courseId, Integer orderIndex);
}
