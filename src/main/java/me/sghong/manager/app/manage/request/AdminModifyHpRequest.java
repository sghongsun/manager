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
public class AdminModifyHpRequest {
    @NotBlank(message = "아이디를 입력하여 주세요.")
    private String adminid;

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
