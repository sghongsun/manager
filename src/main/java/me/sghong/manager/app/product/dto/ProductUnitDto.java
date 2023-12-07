package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("productUnitDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductUnitDto {
    private int productcode;
    private int unitproductcode;
    private int qty;
    private int unitsaleprice;
    private int unitempprice;
    private int unitbizprice;
    private String standardflag;
    private String createid;
    private String createip;
}
