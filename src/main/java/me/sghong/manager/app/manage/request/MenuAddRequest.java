package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuAddRequest {
    @NotBlank(message = "올바른 값이 아닙니다.")
    @Size(min=4, max=4, message = "올바른 값이 아닙니다.")
    private String menuPCode;

    @NotBlank(message = "메뉴명을 입력하여 주세요.")
    @Size(min=2, max=20, message = "메뉴명을 정확히 입력하여 주세요.")
    private String menuName;

    @NotBlank(message = "메뉴 URL을 입력하여 주세요.")
    private String menuUrl;
}
