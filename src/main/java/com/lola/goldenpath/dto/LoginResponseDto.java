package com.lola.goldenpath.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    private final String tokenType = "Bearer";
    private String jwtCode;

    public LoginResponseDto(final String jwtCode) {
        this.jwtCode = jwtCode;
    }
}
