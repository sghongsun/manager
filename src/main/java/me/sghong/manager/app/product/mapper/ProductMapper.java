package me.sghong.manager.app.product.mapper;

import me.sghong.manager.app.product.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ProductMapper {
    int select_product_by_list_for_totalcount(HashMap<String, Object> hashMap);
    List<ProductListDto> select_product_by_list(HashMap<String, Object> hashMap);
    List<ReleasecenterDto> select_releasecenter_by_list_for_use();
    List<ProductGosiCategoryDto> select_product_gosi_category1_by_list_for_use();
    ProductDto select_product_by_productcode(int productCode);
    void update_product_by_productcode_for_state(ProductDto productDto);
    void insert_product_history_by_productcode(int productCode);
    List<ProductGosiDto> select_product_gosi_category2_by_code1(String code1);
    List<ProductGosiDto> select_products_gosi_by_productcode(int productCode);
    ProductGosiCategoryDto select_product_gosi_category1_by_code1(String code1);
    int select_product_gosi_category2_by_code1_for_count(String code1);
    ProductDto select_product_by_itemNo(String itemNo);
    void insert_product(ProductDto productDto);
    void insert_product_gosi(ProductGosiDto productGosiDto);
    void insert_product_unit(ProductUnitDto productUnitDto);
    void insert_product_stock(ProductStockDto productStockDto);
    void insert_product_image(ProductImageDto productImageDto);
    List<ProductListDto> select_product_by_productcodesin(String[] productCode);
    void delete_product_setproduct_temp_by_createid(String createid);
    void insert_product_setproduct_temp_by_createid(int productCode, int qty, String createid, String createip);
    int select_product_setproduct_temp_by_createid_for_count(String createid);
    ProductDto select_product_setproduct_by_createid_for_dupcheck(String createid, int count);
    void update_product_by_productcode_for_itemno_set(int productCode, String itemNo);

}
