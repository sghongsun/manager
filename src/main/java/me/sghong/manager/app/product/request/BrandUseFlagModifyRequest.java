package me.sghong.manager.app.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BrandUseFlagModifyRequest {
    @NotBlank(message = "브랜드 정보가 없습니다.")
    private String brandCode;

    @NotBlank(message = "사용여부 정보가 없습니다.")
    private String useFlag;
}
