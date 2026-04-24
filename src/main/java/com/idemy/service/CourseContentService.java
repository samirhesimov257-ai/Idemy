package com.idemy.service;

import com.idemy.dao.entity.Course;
import com.idemy.dao.entity.Lesson;
import com.idemy.dao.entity.Section;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dao.repository.LessonRepository;
import com.idemy.dao.repository.SectionRepository;
import com.idemy.dto.request.LessonRequest;
import com.idemy.dto.request.SectionRequest;
import com.idemy.dto.responce.LessonResponse;
import com.idemy.dto.responce.SectionResponse;
import com.idemy.mapper.LessonMapper;
import com.idemy.mapper.SectionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CourseContentService {

    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper ;
    private final SectionMapper sectionMapper ;
    private final FileService fileService;

    // BÖLMƏ ƏLAVƏ ETMƏK
    public SectionResponse addSection(Long courseId, SectionRequest request) {
        if (sectionRepository.existsByCourseIdAndOrderIndex(courseId, request.getOrderIndex())) {
            throw new RuntimeException("Bu sıra nömrəsi artıq istifadə olunub: " + request.getOrderIndex());
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));
        
        checkOwnership(course);

        int order = (request.getOrderIndex() != null) ? request.getOrderIndex() :
                     (int) sectionRepository.countByCourseId(courseId) + 1;

        Section section = new Section();
        section.setTitle(request.getTitle());
        section.setOrderIndex(order);
        section.setCourse(course);

        return sectionMapper.toDtoRes(sectionRepository.save(section));
    }

    // DƏRS ƏLAVƏ ETMƏK (VİDEO İLE)
    public LessonResponse addLesson(Long sectionId, LessonRequest request) {

        if (lessonRepository.existsBySectionIdAndOrderIndex(sectionId, request.getOrderIndex())) {
            throw new RuntimeException("Bu sıra nömrəsi artıq istifadə olunub: " + request.getOrderIndex());
        }


        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Bölmə tapılmadı"));
        
        checkOwnership(section.getCourse());

        // Videonu lokal yaddaşa yazırıq
        String videoFileName = fileService.saveFile(request.getVideo());

        int order = (request.getOrderIndex() != null) ? request.getOrderIndex() :
                     (int) lessonRepository.countBySectionId(sectionId) + 1;

        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setDurationInMinutes(request.getDurationInMinutes());
        lesson.setVideoUrl(request.getVideo().getOriginalFilename());
        lesson.setOrderIndex(order);
        lesson.setSection(section);

        return lessonMapper.toDtoLessonRes(lessonRepository.save(lesson));
    }

    private void checkOwnership(Course course) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!course.getInstructor().getEmail().equals(email)) {
            throw new RuntimeException("Bu kurs sizin deyil!");
        }
    }
}