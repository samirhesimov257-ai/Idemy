package com.idemy.controller;

import com.idemy.dao.entity.Course;
import com.idemy.dao.repository.CourseRepository;
import com.idemy.dto.responce.CourseResponse;
import com.idemy.mapper.CourseMapper;
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

    // Hər kəs üçün açıq axtarış
    @GetMapping("/search")
    public ResponseEntity<List<CourseResponse>> searchCourses(@RequestParam String keyword) {
        return ResponseEntity.ok(courseMapper.listToDtoList(courseRepository.searchByKeyword(keyword)));
    }

    // Kursun bütün detalları (Bölmələr və s.)
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseDetails(@PathVariable Long id) {
        return ResponseEntity.ok(courseMapper.toDto(courseRepository.findById(id).orElseThrow()));
    }
}