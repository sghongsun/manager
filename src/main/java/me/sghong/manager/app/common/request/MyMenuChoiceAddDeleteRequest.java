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
public class MyMenuChoiceAddDeleteRequest {
    @NotBlank(message = "잘못된 요청 입니다.")
    @Size(min=4, max=4, message = "잘못된 요청 입니다.")
    private String menucode;
}
