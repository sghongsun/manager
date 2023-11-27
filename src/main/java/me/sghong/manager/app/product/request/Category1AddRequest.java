package me.sghong.manager.app.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category1AddRequest {
    @NotBlank(message = "대분류명을 입력하여 주세요.")
    private String categoryName;

    @NotBlank(message = "게시여부를 선택하여 주세요.")
    private String displayFlag;
}
