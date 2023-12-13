package me.sghong.manager.app.product.dto;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.util.ArrayList;

@Alias("productExcelAddDto")
@Getter
@Setter
public class ProductExcelAddDto {
    private String itemNo;
    private String categorycode2;
    private String categorycode1;
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
    private int minsalecnt;
    private int maxsalecnt;
    private int qty;
    private String offflag;
    private String salestate;
    private String brandcode;
    private String keyword;
    private String description;
    private String fixdelvflag;
    private String freedelvflag;
    private String taxtype;
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
    private String image1;
    private String image2;
    private String image3;
    private ArrayList<String> gosi;
    private String errcode;
    private String errmsg;
    private String createid;
    private String createip;
}
