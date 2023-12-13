package me.sghong.manager.app.product.dto;

import lombok.*;
import me.sghong.manager.app.common.dto.SearchDto;
import org.apache.ibatis.type.Alias;

@Alias("productExcelAddErrorSearchDto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductExcelAddErrorSearchDto extends SearchDto {
    @Setter
    private String createid;
}
