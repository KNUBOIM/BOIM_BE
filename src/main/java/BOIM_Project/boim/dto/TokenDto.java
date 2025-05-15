package BOIM_Project.boim.dto;

import lombok.Builder;

@Builder
public record TokenDto(String accessToken, String refreshToken) {

    public static TokenDto create(String accessToken, String refreshToken) {

        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
