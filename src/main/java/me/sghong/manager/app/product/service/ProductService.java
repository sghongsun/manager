package me.sghong.manager.app.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.Pagination;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.product.dto.ProductGosiCategoryDto;
import me.sghong.manager.app.product.dto.ProductListDto;
import me.sghong.manager.app.product.dto.ProductSearchDto;
import me.sghong.manager.app.product.dto.ReleasecenterDto;
import me.sghong.manager.app.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;

    public PagingDto<ProductListDto> getProductList(ProductSearchDto productSearchDto) {
        if (productSearchDto.getKeyword() != null && !productSearchDto.getKeyword().isEmpty()) {
            productSearchDto.setKeywords(productSearchDto.getKeyword().split("\r\n"));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, Object> map = objectMapper.convertValue(productSearchDto, HashMap.class);

        int count = productMapper.select_product_by_list_for_totalcount(map);

        if (count < 1) {
            return new PagingDto<>(Collections.emptyList(), null);
        }

        Pagination pagination = new Pagination(count, productSearchDto);
        productSearchDto.setPagination(pagination);

        map.put("limitstart", pagination.getLimitStart());

        List<ProductListDto> list = productMapper.select_product_by_list(map);
        return new PagingDto<>(list, pagination);
    }

    public List<ReleasecenterDto> getReleaseCenterList() {
        return productMapper.select_releasecenter_by_list_for_use();
    }

    public List<ProductGosiCategoryDto> getProductGosiCategory1List() {
        return productMapper.select_product_gosi_category1_by_list_for_use();
    }
}
