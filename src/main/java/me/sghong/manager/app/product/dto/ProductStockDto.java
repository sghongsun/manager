package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("productStockDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProductStockDto {
    private int productcode;
    private String itemNo;
    private int stockqty;
    private int saleqty;
    private int outsaleqty;
    private int restqty;
    private String createid;
    private String createip;
}
