package me.sghong.manager.app.product.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductDto {
    private int productcode;
    private String itemNo;
    private String categorycode1;
    private String categorycode2;
    private String productclass;
    private String marketingword;
    private String productname;
    private String standard;
    private String productshortname;
    private String onlineflag;
    private String empflag;
    private String bizflag;
    private int costprice;
    private int agencyprice;
    private int tagprice;
    private int saleprice;
    private int empprice;
    private int bizprice;
    private int qty;
    private int minsalecnt;
    private int maxsalecnt;
    private String salestate;
    private String offflag;
    private String brandcode;
    private String keyword;
    private String description;
    private String fixdelvflag;
    private String freedelvflag;
    private String taxtype;
    private int saleqty;
    private int viewcnt;
    private int reviewcnt;
    private String productkind;
    private String producttype;
    private String productgubn;
    private String makeyear;
    private String makedm;
    private String maker;
    private String origincode;
    private String origin;
    private String sex;
    private String stockupflag;
    private String releasecentercode;
    private String releaseproductcode;
    private String createid;
    private String createip;
}
