package me.sghong.manager.app.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductListDto extends ProductDto {
    private String brandname;
    private String imgurl;
    private String standardflag;
    private int stock;
    private int stockqty;
    private int shopsaleqty;
    private int outsaleqty;
    private int restqty;
    private LocalDateTime createdt;
}
