package com.idemy.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseCreateRequest {
    private String title;
    private String description;
    private BigDecimal price;
}