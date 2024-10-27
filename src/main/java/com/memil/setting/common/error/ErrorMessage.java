package com.memil.setting.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessage {
    WRONG_USERNAME("아이디를 확인해주세요"),
    WRONG_PASSWORD("비밀번호를 확인해주세요"),
    INVALID_ACCESS_TOKEN("유효하지 않은 액세스 토큰입니다"),
    CANNOT_LOGIN("로그인 할 수 없음"),
    ACCESS_TOKEN_EXPIRED("액세스 토큰 만료, 재발급 해주세요."),
    REFRESH_TOKEN_EXPIRED("리프레쉬 토큰 만료. 다시 로그인해주세요"),
    UNEXPECTED_ERROR_OCCUR("알 수 없는 오류 발생!"),
    REFRESH_TOKEN_NOT_FOUND("Redis에 리프레쉬 토큰이 존재하지 않습니다."),
    NO_REFRESH_TOKEN("리프레쉬 토큰이 존재하지 않습니다."),
    INVALID_ENDPOINT_OR_REQUEST("잘못된 요청 혹은 로그인이 필요한 엔드포인트입니다."),
    USER_NOT_FOUND("해당 유저를 찾을 수 없습니다.");

    String message;
}