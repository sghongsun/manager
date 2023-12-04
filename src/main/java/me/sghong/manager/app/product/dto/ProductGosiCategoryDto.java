package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("productGosiCategoryDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductGosiCategoryDto {
    private String code1;
    private String name1;
    private String code2;
    private String name2;
    @Setter
    private String createid;
    @Setter
    private String createip;
}
