package me.sghong.manager.app.product.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BrandAddRequest {
    @NotBlank(message = "브랜드 코드를 입력하여 주세요.")
    private String brandCode;

    @NotBlank(message = "브랜드 명을 입력하여 주세요.")
    private String brandName;

    private MultipartFile logoImg;
    private MultipartFile visualImg;
    private MultipartFile mlogoImg;
    private MultipartFile mvisualImg;
    private String brandStory;
}
