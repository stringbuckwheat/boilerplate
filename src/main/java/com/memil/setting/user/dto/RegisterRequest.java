package com.memil.setting.user.dto;

import com.memil.setting.common.enums.Provider;
import com.memil.setting.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class RegisterRequest {
    @NotBlank(message = "아이디는 비워둘 수 없어요.")
    @Size(min = 3, max = 20, message = "아이디는 3자 이상 20자 이하여야 합니다.")
    private String username;

    @Setter
    @NotBlank(message = "비밀번호는 비워둘 수 없어요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*+=()]).*$",
            message = "비밀번호는 숫자, 문자, 특수문자(!@#$%^&*+=())를 모두 포함해야합니다.")
    private String password;

    @NotBlank(message = "이름은 비워둘 수 없습니다.")
    private String name;

    @Email(message = "이메일 형식을 확인해주세요")
    private String email;

    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .name(name)
                .email(email)
                .provider(Provider.LOCAL)
                .build();
    }
}