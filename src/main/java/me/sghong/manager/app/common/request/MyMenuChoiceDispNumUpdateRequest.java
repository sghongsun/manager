package me.sghong.manager.app.common.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyMenuChoiceDispNumUpdateRequest {
    @NotBlank(message = "구분 값이 없습니다.")
    @Size(min=1, max=1, message = "구분값이 잘못 되었습니다.")
    private String udType;

    @NotBlank(message = "코드 값이 없습니다.")
    @Size(min=4, max=4, message = "코드 값이 잘못 되었습니다.")
    private String menucode;
}
