package com.idemy.controller;

import com.idemy.dao.entity.Course;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dto.responce.CourseDetailResponse;
import com.idemy.dto.responce.CourseResponse;
import com.idemy.mapper.CourseMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseDiscoveryController {

    private final CourseRepository courseRepository;
    private  final CourseMapper courseMapper ;


    @GetMapping("/search")
    public ResponseEntity<List<CourseResponse>> searchCourses(@RequestParam String keyword) {
        return ResponseEntity.ok(courseMapper.listToDtoList(courseRepository.searchByKeyword(keyword)));
    }
    @GetMapping("/{id}/syllabus")
    public ResponseEntity<CourseDetailResponse> getSyllabus(@PathVariable Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kurs tapılmadı"));
        return ResponseEntity.ok(courseMapper.toDtoDetail(course));
    }
    @Operation(summary = "Muellim adina gore kurslarin tapilmasi")
    @GetMapping("/search-by-instructor")
    public ResponseEntity<List<CourseResponse>> searchByInstructor(@RequestParam String name) {
        List<Course> courses = courseRepository.findByInstructorFullNameContainingIgnoreCase(name);

        // Entity-ləri DTO-ya (Response) çevirib qaytarırıq
        return ResponseEntity.ok(courses.stream()
                .map(courseMapper::toDto)
                .toList());
    }

    // Kursun bütün detalları (Bölmələr və s.)
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseDetails(@PathVariable Long id) {
        return ResponseEntity.ok(courseMapper.toDto(courseRepository.findById(id).orElseThrow()));
    }
}