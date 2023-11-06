package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminModifyPwdRequet {
    @NotBlank(message = "아이디를 입력하여 주세요.")
    private String adminid;

    @NotBlank(message = "비밀번호를 입력하여 주세요.")
    @Size(min=4, max=12, message = "비밀번호를 입력하여 주세요.")
    private String pwd;

    @NotBlank(message = "비밀번호확인을 입력하여 주세요.")
    @Size(min=4, max=12, message = "비밀번호확인을 입력하여 주세요.")
    private String pwd1;

}
