package com.memil.setting.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserUpdate {
    @NotBlank(message = "이름은 비워둘 수 없어요")
    private String name;

    @Email(message = "이메일 형식을 확인해주세요")
    private String email;
}
