package me.sghong.manager.app.product.dto;

import lombok.*;
import me.sghong.manager.app.common.dto.SearchDto;
import org.apache.ibatis.type.Alias;

@Alias("brandSearchDto")
@Getter
public class BrandSearchDto extends SearchDto {
    private String useflag;

    public BrandSearchDto(String useflag) {
        this.useflag = useflag;
        setSearchtype("brandname");
    }

}
