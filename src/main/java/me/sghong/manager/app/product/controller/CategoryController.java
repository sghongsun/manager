package me.sghong.manager.app.product.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.product.dto.CategoryDto;
import me.sghong.manager.app.product.request.*;
import me.sghong.manager.app.product.service.CategoryService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/product/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String categoryList(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
       model.addAttribute("Permission", permissionDto);
       model.addAttribute("category1List", categoryService.getCategory1ListInProductCnt());
       model.addAttribute("category2List", categoryService.getCategory2ListInProductCnt());
       return "product/category/list";
    }

    @GetMapping("/list1")
    public String category1List(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("category1List", categoryService.getCategory1ListInProductCnt());
        return "product/category/list1";
    }

    @GetMapping("/list2")
    public String category2List(
            Model model,
            @Permission PermissionDto permissionDto,
            @RequestParam(required = false) String CategoryCode1
    ) {
        model.addAttribute("Permission", permissionDto);

        List<CategoryDto> category1List = categoryService.getCategory1List();
        if (CategoryCode1 == null || CategoryCode1.isEmpty()) {
            CategoryCode1 = category1List.get(0).getCategorycode1();
        }

        model.addAttribute("categoryCode1", CategoryCode1);
        model.addAttribute("category1List", category1List);
        model.addAttribute("category2List", categoryService.getCategory2List(CategoryCode1));
        return "product/category/list2";
    }

    @PostMapping("/ajax/addform")
    public String categoryAdd(
            Model model,
            @RequestParam String Depth
    ) {
        model.addAttribute("depth", Depth);
        if (Depth.equals("2")) {
            model.addAttribute("category1List", categoryService.getCategory1List());
        }
        return "product/category/ajax/add";
    }

    @PostMapping("/ajax/add1")
    public void category1AddOk(
            HttpServletResponse response,
            @Validated Category1AddRequest category1AddRequest
    ) throws IOException {
        categoryService.insertCategory1(category1AddRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("OK|||||");
        out.flush();
    }

    @PostMapping("/ajax/add2")
    public void category2AddOk(
            HttpServletResponse response,
            @Validated Category2AddRequest category2AddRequest
    ) throws IOException {
        categoryService.insertCategory2(category2AddRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("OK|||||");
        out.flush();
    }

    @PostMapping("/ajax/modifyform")
    public String categoryModify(
            Model model,
            @RequestParam String Depth,
            @RequestParam String CategoryCode1,
            @RequestParam(required = false) String CategoryCode2
    ) {
        model.addAttribute("depth", Depth);
        if (Depth.equals("1")) {
            model.addAttribute("category", categoryService.getCategory1Info(CategoryCode1));
        } else {
            model.addAttribute("category", categoryService.getCategory2Info(CategoryCode1, CategoryCode2));
        }
        return "product/category/ajax/modify";
    }

    @PostMapping("/ajax/modify1")
    public void category1ModifyOk(
            HttpServletResponse response,
            @Validated Category1ModifyRequest category1ModifyRequest
    ) throws IOException {
        categoryService.updateCategory1(category1ModifyRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("OK|||||");
        out.flush();
    }

    @PostMapping("/ajax/modify2")
    public void category2ModifyOk(
            HttpServletResponse response,
            @Validated Category2ModifyRequest category2ModifyRequest
    ) throws IOException {
        categoryService.updateCategory2(category2ModifyRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("OK|||||");
        out.flush();
    }

    @PostMapping("/ajax/displaynum_modify1")
    public void category1DisplayNumModifyOk(
            HttpServletResponse response,
            @Validated Category1DisplayNumModifyRequest category1DisplayNumModifyRequest
    ) throws IOException {
        categoryService.updateCategory1DisplayNum(category1DisplayNumModifyRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("OK|||||");
        out.flush();
    }

    @PostMapping("/ajax/displaynum_modify2")
    public void category2DisplayNumModifyOk(
            HttpServletResponse response,
            @Validated Category2DisplayNumModityRequest category2DisplayNumModityRequest
    ) throws IOException {
        categoryService.updateCategory2DisplayNum(category2DisplayNumModityRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("OK|||||");
        out.flush();
    }
}
