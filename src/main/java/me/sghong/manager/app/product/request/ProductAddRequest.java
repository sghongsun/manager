package me.sghong.manager.app.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductAddRequest {
    private String setproductcodes;
    private String setunitcostprices;
    private String setunitagencyprices;
    private String setunittagprices;
    private String setunitsaleprices;
    private String setunitempprices;
    private String setunitbizprices;
    private String setunitqtys;
    private String groupproductcodes;
    private String groupstandardflags;
    private String onlineflag;
    private String bizflag;
    private String empflag;
    private String itemNo;
    @NotBlank(message = "상품분류를 선택하여 주세요.")
    private String categorycode1;
    @NotBlank(message = "상품분류를 선택하여 주세요.")
    private String categorycode2;
    @NotBlank(message = "상품구분을 선택하여 주세요.")
    private String productclass;
    @NotBlank(message = "출고지를 선택하여 주세요.")
    private String releasecentercode;
    private String marketingword;
    @NotBlank(message = "상품명을 입력하여 주세요.")
    private String productname;
    private String standard;
    @NotBlank(message = "과/면세를 선택하여 주세요.")
    private String taxtype;
    @NotBlank(message = "브랜드를 선택하여 주세요.")
    private String brandcode;
    @NotBlank(message = "관리형태를 선택하여 주세요.")
    private String productgubn;
    @NotBlank(message = "원사지를 입력하여 주세요.")
    private String origin;
    private String maker;
    @PositiveOrZero(message = "최소판매수량을 숫자로 입력하여 주세요.")
    private int minsalecnt;
    @PositiveOrZero(message = "최대판매수량을 숫자로 입력하여 주세요.")
    private int maxsalecnt;
    @NotBlank(message = "배송비무료 여부를 선택하여 주세요.")
    private String freedelvflag;
    @NotBlank(message = "정기배송 여부를 선택하여 주세요.")
    private String fixdelvflag;
    @NotBlank(message = "판매상태를 선택하여 주세요.")
    private String salestate;
    @PositiveOrZero(message = "사입가를 숫자로 입력하여 주세요.")
    private int costprice;
    @PositiveOrZero(message = "대리점가를 숫자로 입력하여 주세요.")
    private int agencyprice;
    @PositiveOrZero(message = "정상가를 숫자로 입력하여 주세요.")
    private int tagprice;
    @PositiveOrZero(message = "판매가를 숫자로 입력하여 주세요.")
    private int saleprice;
    @PositiveOrZero(message = "임직원가를 숫자로 입력하여 주세요.")
    private int empprice;
    @PositiveOrZero(message = "사업자회원가를 숫자로 입력하여 주세요.")
    private int bizprice;
    private String keyword;
    private String description;
    private MultipartFile image1;
    private MultipartFile image2;
    private MultipartFile image3;
    @NotBlank(message = "정보고시 항목을 선택하여 주세요.")
    private String producttype;
    private ArrayList<String> gosi;
}
