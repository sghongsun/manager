package me.sghong.manager.app.product.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupProductSearchDto extends ProductSearchDto {
    private String productcodes;
    private String standardflags;

    public GroupProductSearchDto() {
        setProductclass(new String[]{"P", "S"});
        setSearchtype("productname");
    }
}
