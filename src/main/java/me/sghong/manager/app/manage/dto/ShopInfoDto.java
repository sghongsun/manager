package me.sghong.manager.app.manage.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("shopInfoDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ShopInfoDto {
    private int standardprice;
    private int deliveryprice;
    private int returndeliveryprice;
    private int changedeliveryprice;
    private int foreignstandardprice;
    private int foreigndeliveryprice;
    private int foreignreturndeliveryprice;
    private int foreignchangedeliveryprice;
    private String rzipcode;
    private String raddr1;
    private String raddr2;
    private String foreignrzipcode;
    private String foreignraddr1;
    private String foreignraddr2;
    private int txtreviewpoint;
    private int imgreviewpoint;
    private String createid;
    private String createip;
}
