package com.idemy.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String accessToken;
    private String refreshToken;
    
    // Əgər istəsən, istifadəçinin rolunu və ya adını da bura əlavə edə bilərsən
    // private String fullName;
    // private String role;
}