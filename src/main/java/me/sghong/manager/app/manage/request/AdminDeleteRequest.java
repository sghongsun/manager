package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminDeleteRequest {
    @NotBlank(message = "아이디를 입력하여 주세요.")
    private String adminid;
}
