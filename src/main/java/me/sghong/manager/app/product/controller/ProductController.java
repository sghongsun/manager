package me.sghong.manager.app.product.controller;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.product.dto.ProductSearchDto;
import me.sghong.manager.app.product.service.ProductService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/product/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/list")
    public String productList(
            Model model,
            @Permission PermissionDto permissionDto,
            @ModelAttribute("searchDto")ProductSearchDto productSearchDto
    ) {
        model.addAttribute("Permission", permissionDto);

        return "product/product/list";
    }
}
