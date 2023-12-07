package me.sghong.manager.app.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductStateModifyRequest {
    @NotBlank(message = "상품 정보가 없습니다.")
    private String productCode;

    @NotBlank(message = "품절 정보가 없습니다.")
    private String offFlag;

    @NotBlank(message = "판매 정보가 없습니다.")
    private String saleState;
}
