package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminAddRequest {
    @NotBlank(message = "아이디를 입력하여 주세요.")
    @Size(min=4, max=12, message = "아이디를 정확히 입력하여 주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "아이디를 영숫자로만 입력하여 주세요.")
    private String adminid;

    @NotBlank(message = "아이디 중복 체크를 해 주세요.")
    @Size(min=1, max=1, message = "아이디 중복 체크를 해 주세요.")
    private String checkidAvailable;

    @NotBlank(message = "비밀번호를 입력하여 주세요.")
    @Size(min=4, max=12, message = "비밀번호를 입력하여 주세요.")
    private String pwd;

    @NotBlank(message = "비밀번호확인을 입력하여 주세요.")
    @Size(min=4, max=12, message = "비밀번호확인을 입력하여 주세요.")
    private String pwd1;

    @Positive(message = "그룹을 선택하여 주세요.")
    private int groupcode;

    @NotBlank(message = "이름을 입력하여 주세요.")
    @Size(min=2, message = "이름을 입력하여 주세요.")
    private String name;

    @NotBlank(message = "핸드폰번호를 선택하여 주세요.")
    @Size(min=3, max=3, message = "핸드폰번호를 선택하여 주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "핸드폰번호는 숫자만 입력 가능 합니다.")
    private String hp1;

    @NotBlank(message = "핸드폰번호를 입력하여 주세요.")
    @Size(min=3, max=4, message = "핸드폰번호를 입력하여 주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "핸드폰번호는 숫자만 입력 가능 합니다.")
    private String hp2;

    @NotBlank(message = "핸드폰번호를 입력하여 주세요.")
    @Size(min=4, max=4, message = "핸드폰번호를 입력하여 주세요.")
    @Pattern(regexp = "^[0-9]*$", message = "핸드폰번호는 숫자만 입력 가능 합니다.")
    private String hp3;
}
