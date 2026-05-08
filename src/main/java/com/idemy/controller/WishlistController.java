package com.idemy.controller;

import com.idemy.dto.responce.CourseResponse;
import com.idemy.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @PostMapping("/{courseId}")
    public ResponseEntity<String> addToWishlist(@PathVariable Long courseId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(courseId));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getWishlist() {
        return ResponseEntity.ok(wishlistService.getWishlist());
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable Long courseId) {
        return ResponseEntity.ok(wishlistService.removeFromWishlist(courseId));
    }
}
