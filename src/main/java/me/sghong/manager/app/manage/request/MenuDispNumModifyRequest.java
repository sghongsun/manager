package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuDispNumModifyRequest {
    @NotBlank(message = "올바른 값이 아닙니다.")
    @Size(min=1, max=1, message = "올바른 값이 아닙니다.")
    private String udType;

    @NotBlank(message = "올바른 값이 아닙니다.")
    @Size(min=4, max=4, message = "올바른 값이 아닙니다.")
    private String menuPCode;

    @NotBlank(message = "올바른 값이 아닙니다.")
    @Size(min=4, max=4, message = "올바른 값이 아닙니다.")
    private String menuCode;
}
