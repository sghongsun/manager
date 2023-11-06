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
public class AdminModifyRequest {
    @NotBlank(message = "아이디를 입력하여 주세요.")
    private String adminid;

    @Positive(message = "그룹을 선택하여 주세요.")
    private int groupcode;

    @NotBlank(message = "이름을 입력하여 주세요.")
    @Size(min=2, message = "이름을 입력하여 주세요.")
    private String name;
}
