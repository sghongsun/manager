package me.sghong.manager.app.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("productExcelAddErrorDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductExcelAddErrorDto {
    private String itemNo;
    private String productname;
    private String standard;
    private int tagprice;
    private int saleprice;
    private String errcode;
    private String errmsg;
}
