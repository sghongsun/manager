package me.sghong.manager.app.product.mapper;

import me.sghong.manager.app.product.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<CategoryDto> select_by_category1_list_for_productcnt();
    List<CategoryDto> select_by_category2_list_for_productcnt();
    List<CategoryDto> select_by_category1_list();
    List<CategoryDto> select_by_category2_list(String categoryCode1);
    int select_by_category1_for_max_code();
    int select_by_category2_for_max_code(String categoryCode1);
    int select_by_category1_for_max_displaynum();
    int select_by_category2_for_max_displaynum(String categoryCode1);
    void insert_category1(CategoryDto categoryDto);
    void insert_category2(CategoryDto categoryDto);
    CategoryDto select_by_category1(String categoryCode1);
    CategoryDto select_by_category2(String categoryCode1, String categoryCode2);
    void update_category1(CategoryDto categoryDto);
    void update_category2(CategoryDto categoryDto);
    CategoryDto select_by_category1_for_displaynum_up(int displayNum);
    CategoryDto select_by_category1_for_displaynum_down(int displayNum);
    CategoryDto select_by_category2_for_displaynum_up(String categoryCode1, int displayNum);
    CategoryDto select_by_category2_for_displaynum_down(String categoryCode1, int displayNum);
    void update_category1_displaynum(CategoryDto categoryDto);
    void update_category2_displaynum(CategoryDto categoryDto);
}
