package com.idemy.dao.entity;

import com.idemy.util.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String profileImageUrl;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Course> authoredCourses;

    // Tələbənin qeydiyyatdan keçdiyi kurslar (Day 5-dən)
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    // İstifadəçinin yazdığı bütün rəylər (Day 6)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    // İstifadəçinin dərslərdəki proqresi (Day 6)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserProgress> progressEntries;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Rolu "ROLE_STUDENT" və ya "ROLE_INSTRUCTOR" kimi qaytarır
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email; // Login üçün email istifadə edəcəyik
    }

    @Override
    public String getPassword() {
        return password; // Sənin yuxarıdakı password field-in
    }

}
