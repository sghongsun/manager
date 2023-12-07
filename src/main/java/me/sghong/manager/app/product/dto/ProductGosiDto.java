package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("productGosiDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductGosiDto {
    private int productcode;
    private String code1;
    private String name1;
    private String code2;
    private String name2;
    private String contents;
    private String createid;
    private String createip;
}
