package com.idemy.dao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Dərs adı boş ola bilməz")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content; // Dərs haqqında əlavə qeydlər və ya mətn

    private String videoUrl; // AWS S3-dən gələn video linki burada olacaq

    private Duration durationInMinutes; // Dərsin müddəti

    private Integer orderIndex; // Bölmə daxilində neçənci dərsdir

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;
}