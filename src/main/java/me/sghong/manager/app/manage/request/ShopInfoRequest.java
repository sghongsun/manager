package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShopInfoRequest {
    @PositiveOrZero(message = "배송비 기준 금액을 숫자로만 입력해 주십시오.")
    private int standardprice;

    @PositiveOrZero(message = "배송비 금액을 숫자로만 입력해 주십시오.")
    private int deliveryprice;

    @PositiveOrZero(message = "반품 배송비 금액을 숫자로만 입력해 주십시오.")
    private int returndeliveryprice;

    @PositiveOrZero(message = "교환 배송비 금액을 숫자로만 입력해 주십시오.")
    private int changedeliveryprice;

    @PositiveOrZero(message = "해외 배송비 기준 금액을 숫자로만 입력해 주십시오.")
    private int foreignstandardprice;

    @PositiveOrZero(message = "해외 배송비 금액을 숫자로만 입력해 주십시오.")
    private int foreigndeliveryprice;

    @PositiveOrZero(message = "해외 반품 배송비 금액을 숫자로만 입력해 주십시오.")
    private int foreignreturndeliveryprice;

    @PositiveOrZero(message = "해외 교환 배송비 금액을 숫자로만 입력해 주십시오.")
    private int foreignchangedeliveryprice;

    @NotBlank(message = "반송지 우편번호를 입력해 주십시오.")
    private String rzipcode;

    @NotBlank(message = "반송지 주소를 입력해 주십시오")
    private String raddr1;

    @NotBlank(message = "반송지 상세 주소를 입력해 주십시오")
    private String raddr2;

    @NotBlank(message = "해외 반송지 우편번호를 입력해 주십시오.")
    private String foreignrzipcode;

    @NotBlank(message = "해외 반송지 주소를 입력해 주십시오")
    private String foreignraddr1;

    @NotBlank(message = "해외 반송지 상세 주소를 입력해 주십시오")
    private String foreignraddr2;

    @PositiveOrZero(message = "일반상품평 기준 포인트를 숫자로만 입력해 주십시오.")
    private int txtreviewpoint;

    @PositiveOrZero(message = "포토상품평 기준 포인트를 숫자로만 입력해 주십시오.")
    private int imgreviewpoint;
}
