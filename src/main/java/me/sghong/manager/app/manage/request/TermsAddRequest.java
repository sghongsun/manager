package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TermsAddRequest {
    @NotBlank(message = "제목을 입력하여 주세요.")
    private String title;

    @NotBlank(message = "적용위치를 입력하여 주세요.")
    private String place;

    @NotBlank(message = "내용을 입력하여 주세요.")
    private String contents;
}
