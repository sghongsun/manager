package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("productImageDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductImageDto {
    private int productcode;
    private int imageno;
    private String sizeclass;
    private String imageurl;
    private String createid;
    private String createip;
}
