//package com.idemy.service;
//
//import com.idemy.dao.entity.Course;
//import com.idemy.dao.entity.Lesson;
//import com.idemy.dao.entity.Section;
//import com.idemy.dao.repository.CourseRepository;
//import com.idemy.dao.repository.LessonRepository;
//import com.idemy.dao.repository.SectionRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//@RequiredArgsConstructor
//public class CourseContentServiceSAmir {
//
//    private final SectionRepository sectionRepository;
//    private final LessonRepository lessonRepository;
//    private final CourseRepository courseRepository;
//    private final FileService fileService;
//
//    public Section addSection(Long courseId, String title) {
//        Course course = courseRepository.findById(courseId).orElseThrow();
//
//        // Ownership Check
//        checkOwnership(course);
//
//        Section section = new Section();
//        section.setTitle(title);
//        section.setCourse(course);
//        return sectionRepository.save(section);
//    }
//
//    public Lesson addLesson(Long sectionId, String title, MultipartFile video) {
//        Section section = sectionRepository.findById(sectionId).orElseThrow();
//
//        // Ownership Check (Bölmənin aid olduğu kurs üzərindən)
//        checkOwnership(section.getCourse());
//
//        // Videonu lokal yaddaşa yaz və adını götür
//        String videoPath = fileService.saveFile(video);
//
//        Lesson lesson = new Lesson();
//        lesson.setTitle(title);
//        lesson.setVideoUrl(videoPath);
//        lesson.setSection(section);
//        return lessonRepository.save(lesson);
//    }
//
//    private void checkOwnership(Course course) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        if (!course.getInstructor().getEmail().equals(email)) {
//            throw new RuntimeException("Bu kursa məzmun əlavə etmək icazəniz yoxdur!");
//        }
//    }
//}