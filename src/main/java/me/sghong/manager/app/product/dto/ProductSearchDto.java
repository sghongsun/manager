package me.sghong.manager.app.product.dto;

import lombok.*;
import me.sghong.manager.app.common.dto.SearchDto;
import org.apache.ibatis.type.Alias;

@Alias("productSearchDto")
@Getter
@AllArgsConstructor
public class ProductSearchDto extends SearchDto {
    private String usedate;
    private String datetype;
    private String sdate;
    private String edate;
    private String releasecentercode;
    private String categorycode1;
    private String categorycode2;
    private String[] salestate;
    private String offflag;
    private String taxtype;
    private String brandcode;
    private String producttype;
    private String[] productclass;
    private String stockcnt1;
    private String stockcnt2;
    private String onlineflag;
    private String bizflag;
    private String fixdelvflag;
    private String freedelvflag;
    private String orderby;
    @Setter
    private String[] keywords;
}
