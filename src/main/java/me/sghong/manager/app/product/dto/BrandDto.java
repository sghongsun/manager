package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("brandDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BrandDto {
    private int idx;
    private String brandcode;
    private String brandname;
    private String logoimg;
    private String visualimg;
    private String mlogoimg;
    private String mvisualimg;
    private String brandstory;
    private String useflag;
    @Setter
    private int displaynum;
    @Setter
    private String createid;
    @Setter
    private String createip;
}
