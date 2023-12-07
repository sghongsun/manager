package me.sghong.manager.app.product.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SetProductSearchDto extends ProductSearchDto {
    private String productcodes;
    private String unitcostprices;
    private String unitagencyprices;
    private String unittagprices;
    private String unitsaleprices;
    private String unitempprices;
    private String unitbizprices;
    private String unitqtys;

    public SetProductSearchDto() {
        setProductclass(new String[]{"P"});
        setSearchtype("productname");
    }
}
