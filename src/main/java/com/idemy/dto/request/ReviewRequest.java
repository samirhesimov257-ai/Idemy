package com.idemy.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "Reyting mütləqdir")
    @Min(value = 1, message = "Minimum 1 ulduz verməlisiniz")
    @Max(value = 5, message = "Maksimum 5 ulduz verə bilərsiniz")
    private Integer rating;

    private String comment; // Şərh opsional ola bilər, amma reytinq yox
}