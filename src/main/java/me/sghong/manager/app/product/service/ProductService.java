package me.sghong.manager.app.product.service;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.PagingDto;
import me.sghong.manager.app.product.dto.ProductDto;
import me.sghong.manager.app.product.dto.ProductSearchDto;
import me.sghong.manager.app.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductMapper productMapper;

    public void getList(ProductSearchDto productSearchDto) {

    }
}
