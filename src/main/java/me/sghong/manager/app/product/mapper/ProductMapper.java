package me.sghong.manager.app.product.mapper;

import me.sghong.manager.app.product.dto.ProductGosiCategoryDto;
import me.sghong.manager.app.product.dto.ProductListDto;
import me.sghong.manager.app.product.dto.ProductSearchDto;
import me.sghong.manager.app.product.dto.ReleasecenterDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ProductMapper {
    int select_product_by_list_for_totalcount(HashMap<String, Object> hashMap);
    List<ProductListDto> select_product_by_list(HashMap<String, Object> hashMap);
    List<ReleasecenterDto> select_releasecenter_by_list_for_use();
    List<ProductGosiCategoryDto> select_product_gosi_category1_by_list_for_use();
}
