package me.sghong.manager.app.product.controller;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.product.dto.ProductSearchDto;
import me.sghong.manager.app.product.service.BrandService;
import me.sghong.manager.app.product.service.CategoryService;
import me.sghong.manager.app.product.service.ProductService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@RequestMapping("/product/product")
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String productList(
            Model model,
            @Permission PermissionDto permissionDto,
            @ModelAttribute("searchDto")ProductSearchDto productSearchDto
    ) {
        model.addAttribute("Permission", permissionDto);

        model.addAttribute("releasecenterList", productService.getReleaseCenterList());
        model.addAttribute("brandList", brandService.getListAll());
        model.addAttribute("category1List", categoryService.getCategory1List());
        if (productSearchDto.getCategorycode1() != null && !productSearchDto.getCategorycode1().isEmpty()) {
            model.addAttribute("category2List", categoryService.getCategory2List(productSearchDto.getCategorycode1()));
        } else {
            model.addAttribute("category2List", null);
        }
        model.addAttribute("gosiList", productService.getProductGosiCategory1List());
        model.addAttribute("productList", productService.getProductList(productSearchDto));

        return "product/product/list";
    }
}
