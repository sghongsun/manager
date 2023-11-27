package me.sghong.manager.app.product.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

@Alias("categoryDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String categorycode1;
    private String categoryname1;
    private String categorycode2;
    private String categoryname2;
    private String displayflag;
    @Setter
    private int displaynum;
    @Setter
    private String createid;
    @Setter
    private String createip;
    private int productcnt;
    private int productsalecnt;
}
