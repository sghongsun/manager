package me.sghong.manager.app.product.mapper;

import me.sghong.manager.app.product.dto.BrandDto;
import me.sghong.manager.app.product.dto.BrandSearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrandMapper {
    List<BrandDto> select_by_list_for_search(BrandSearchDto brandSearchDto);
    int select_by_list_for_totalcount(BrandSearchDto brandSearchDto);
    int select_by_brandcode_for_exists(String brandCode);
    int select_by_displaynum_max();
    void insert_brand(BrandDto brandDto);
    BrandDto select_by_brandcode(String brandCode);
    void update_brand(BrandDto brandDto);
    void update_brand_useflag(BrandDto brandDto);
    List<BrandDto> select_by_list_for_all();
    BrandDto select_brand_for_displaynum_up(int displayNum);
    BrandDto select_brand_for_displaynum_dn(int displayNum);
    void update_brand_displaynum(BrandDto brandDto);
    void update_brand_for_displaynum_first(BrandDto brandDto);
    void update_brand_for_displaynum_last(BrandDto brandDto);

}
