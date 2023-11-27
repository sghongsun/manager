package me.sghong.manager.app.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category2ModifyRequest {
    @NotBlank(message = "대분류코드를 선택하여 주세요.")
    @Size(min=2, max=2, message = "대분류코드가 잘못 되었습니다.")
    private String categoryCode1;

    @NotBlank(message = "소분류코드를 선택하여 주세요.")
    @Size(min=2, max=2, message = "소분류코드가 잘못 되었습니다.")
    private String categoryCode2;

    @NotBlank(message = "소분류명을 입력하여 주세요.")
    private String categoryName;

    @NotBlank(message = "게시여부를 선택하여 주세요.")
    private String displayFlag;
}
