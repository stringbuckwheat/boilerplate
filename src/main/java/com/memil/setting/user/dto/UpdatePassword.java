package com.memil.setting.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePassword {
    @NotBlank(message = "비밀번호는 비워둘 수 없습니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=()]).*$",
            message = "비밀번호는 숫자, 문자, 특수문자(!@#$%^&*+=())를 모두 포함해야합니다.")
    private String password;
}
