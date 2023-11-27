package me.sghong.manager.app.product.mapper;

import me.sghong.manager.app.product.dto.ProductListDto;
import me.sghong.manager.app.product.dto.ProductSearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    int select_by_list_for_totalcount(ProductSearchDto productSearchDto);
    List<ProductListDto> select_by_list(ProductSearchDto productSearchDto);
}
