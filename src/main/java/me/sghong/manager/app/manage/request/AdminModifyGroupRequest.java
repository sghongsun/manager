package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminModifyGroupRequest {
    @NotBlank(message = "입력값이 없습니다.")
    private String adminid;

    @NotBlank(message = "입력값이 없습니다.")
    private String groupcode;
}
