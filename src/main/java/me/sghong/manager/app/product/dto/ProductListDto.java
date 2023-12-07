package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Alias("productListDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductListDto extends ProductDto {
    private String brandname;
    private String imgurl;
    @Setter
    private String standardflag;
    private int stock;
    private int stockqty;
    private int shopsaleqty;
    private int outsaleqty;
    private int restqty;
    private LocalDateTime createdt;
}
