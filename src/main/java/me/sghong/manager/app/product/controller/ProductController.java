package me.sghong.manager.app.product.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.product.dto.*;
import me.sghong.manager.app.product.request.ProductAddRequest;
import me.sghong.manager.app.product.request.ProductStateModifyRequest;
import me.sghong.manager.app.product.service.BrandService;
import me.sghong.manager.app.product.service.CategoryService;
import me.sghong.manager.app.product.service.ProductService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.View;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    @GetMapping("/product/list")
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

    @PostMapping("/product/ajax/statemodify")
    public void productStateModifyOk(
            HttpServletResponse response,
            @Validated ProductStateModifyRequest productStateModifyRequest
    ) throws IOException {
        String result = productService.updateProductState(productStateModifyRequest);

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        if (result.equals("OK")) {
            out.println("OK|||||");
        } else {
            out.println("FAIL|||||"+result);
        }
        out.flush();
    }

    @PostMapping("/product/ajax/gosiinfo")
    public String GosiInfo(
            Model model,
            @RequestParam(required = false) String ProductCode,
            @RequestParam String ProductType
    ) {
        model.addAttribute("gosicategoryList", productService.getProductGosiList(ProductCode, ProductType));
        return "product/product/ajax/gosilist";
    }

    @GetMapping("/product/add")
    public String productAdd(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);

        model.addAttribute("releasecenterList", productService.getReleaseCenterList());
        model.addAttribute("brandList", brandService.getListAll());
        model.addAttribute("category1List", categoryService.getCategory1List());
        model.addAttribute("category2List", null);
        model.addAttribute("gosiList", productService.getProductGosiCategory1List());
        return "product/product/add";
    }

    @GetMapping("/product/copy")
    public String productCopy(
            Model model,
            @Permission PermissionDto permissionDto,
            @RequestParam String productcode
    ) {
        model.addAttribute("Permission", permissionDto);

        ProductDto productDto = productService.getProductInfo(Integer.parseInt(productcode));

        model.addAttribute("productDto", productDto);

        model.addAttribute("releasecenterList", productService.getReleaseCenterList());
        model.addAttribute("brandList", brandService.getListAll());
        model.addAttribute("category1List", categoryService.getCategory1List());
        model.addAttribute("category2List", categoryService.getCategory2List(productDto.getCategorycode1()));
        model.addAttribute("gosiList", productService.getProductGosiCategory1List());

        return "product/product/copy";
    }

    @PostMapping("/product/add")
    public void ProductAddOk(
            @Validated ProductAddRequest productAddRequest
    ) throws IOException {
        String result = productService.insertProduct(productAddRequest);
        if (result.equals("OK")) {
            CommonUtil.AlertMessage("상품이 등록 되었습니다.", "location.replace('/product/product/list');");
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
        }
    }

    @PostMapping("/product/popup/setproductadd")
    public String setProductAdd(
            Model model,
            @ModelAttribute("searchDto") SetProductSearchDto setProductSearchDto
    ) {
        model.addAttribute("releasecentercodeList", productService.getReleaseCenterList());
        model.addAttribute("category1List", categoryService.getCategory1List());
        if (setProductSearchDto.getCategorycode1() != null && ! setProductSearchDto.getCategorycode1().isEmpty()) {
            model.addAttribute("category2List", categoryService.getCategory2List(setProductSearchDto.getCategorycode1()));
        } else {
            model.addAttribute("category2List", null);
        }
        model.addAttribute("brandList", brandService.getListAll());
        model.addAttribute("gosiList", productService.getProductGosiCategory1List());
        model.addAttribute("productList", productService.getSetProductSearchList(setProductSearchDto));

        List<ProductListDto> productList = productService.getSetProductAddList(setProductSearchDto);
        model.addAttribute("productAddList", productList);

        int TotalTagPrice = 0;
        int TotalSalePrice = 0;
        int TotalEmpPrice = 0;
        int TotalBizPrice = 0;

        if (productList != null) {
            for (ProductListDto productListDto : productList) {
                TotalTagPrice += productListDto.getTagprice() * productListDto.getQty();
                TotalSalePrice += productListDto.getSaleprice();
                TotalEmpPrice += productListDto.getEmpprice();
                TotalBizPrice += productListDto.getBizprice();
            }
        }
        model.addAttribute("TotalTagPrice", TotalTagPrice);
        model.addAttribute("TotalSalePrice", TotalSalePrice);
        model.addAttribute("TotalEmpPrice", TotalEmpPrice);
        model.addAttribute("TotalBizPrice", TotalBizPrice);

        return "product/product/popup/setproductadd";
    }

    @PostMapping("/product/ajax/setproductapplyok")
    public String setProductAddApplyOk(
            Model model,
            SetProductSearchDto setProductSearchDto
    ) throws IOException {
        String result = productService.SetProductApplyCheck(setProductSearchDto);
        if (result.equals("OK")) {
            List<ProductListDto> productList = productService.getSetProductAddList(setProductSearchDto);
            model.addAttribute("productAddList", productList);

            int TotalCostPrice = 0;
            int TotalAgencyPrice = 0;
            int TotalTagPrice = 0;
            int TotalSalePrice = 0;
            int TotalEmpPrice = 0;
            int TotalBizPrice = 0;

            if (productList != null) {
                for (ProductListDto productListDto : productList) {
                    TotalCostPrice += productListDto.getCostprice() * productListDto.getQty();
                    TotalAgencyPrice += productListDto.getAgencyprice() * productListDto.getQty();
                    TotalTagPrice += productListDto.getTagprice() * productListDto.getQty();
                    TotalSalePrice += productListDto.getSaleprice();
                    TotalEmpPrice += productListDto.getEmpprice();
                    TotalBizPrice += productListDto.getBizprice();
                }
            }
            model.addAttribute("TotalCostPrice", TotalCostPrice);
            model.addAttribute("TotalAgencyPrice", TotalAgencyPrice);
            model.addAttribute("TotalTagPrice", TotalTagPrice);
            model.addAttribute("TotalSalePrice", TotalSalePrice);
            model.addAttribute("TotalEmpPrice", TotalEmpPrice);
            model.addAttribute("TotalBizPrice", TotalBizPrice);
            model.addAttribute("Taxtype", productList.get(0).getTaxtype());
            model.addAttribute("Releasecentercode", productList.get(0).getReleasecentercode());

            return "product/product/ajax/setproductapplyok";
        } else {
            CommonUtil.AlertMessage(result, "ajax");
            return "blank";
        }
    }

    @PostMapping("/product/ajax/setproductduplicationcheck")
    public void setProductDuplicationCheck(
            HttpServletResponse response,
            @RequestParam String SetProductCodes,
            @RequestParam String SetUnitQtys
    ) throws IOException {
        String result = productService.SetProductDupCheck(SetProductCodes, SetUnitQtys);

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result+"|||||");
        out.flush();
    }

    @PostMapping("/product/popup/groupproductadd")
    public String groupProductAdd(
            Model model,
            @ModelAttribute("searchDto") GroupProductSearchDto groupProductSearchDto
    ) {
        model.addAttribute("releasecentercodeList", productService.getReleaseCenterList());
        model.addAttribute("category1List", categoryService.getCategory1List());
        if (groupProductSearchDto.getCategorycode1() != null && ! groupProductSearchDto.getCategorycode1().isEmpty()) {
            model.addAttribute("category2List", categoryService.getCategory2List(groupProductSearchDto.getCategorycode1()));
        } else {
            model.addAttribute("category2List", null);
        }
        model.addAttribute("brandList", brandService.getListAll());
        model.addAttribute("gosiList", productService.getProductGosiCategory1List());
        model.addAttribute("productList", productService.getGroupProductSearchList(groupProductSearchDto));
        model.addAttribute("productAddList", productService.getGroupProductAddList(groupProductSearchDto));

        return "product/product/popup/groupproductadd";
    }

    @PostMapping("/product/ajax/groupproductapplyok")
    public String groupProductApplyOk(
            Model model,
            GroupProductSearchDto groupProductSearchDto
    ) throws IOException {
        String result = productService.GroupProductApplyCheck(groupProductSearchDto);
        if (result.equals("OK")) {
            List<ProductListDto> productAddList = productService.getGroupProductAddList(groupProductSearchDto);
            model.addAttribute("productAddList", productAddList);
            model.addAttribute("Taxtype", productAddList.get(0).getTaxtype());
            model.addAttribute("Releasecentercode", productAddList.get(0).getReleasecentercode());
            return "product/product/ajax/groupproductaddapplyok";
        } else {
            CommonUtil.AlertMessage(result, "ajax");
            return "blank";
        }
    }

    @GetMapping("/excel_add")
    public String excelProductAdd(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        return "product/product/excel_add";
    }

    @PostMapping("/excel_add")
    public String excelProductAddOk(
            @RequestParam MultipartFile FileName
    ) throws IOException {
        String result = productService.insertProductExcel(FileName);
        if (result.equals("OK")) {
            return "redirect:/product/excel_add/error";
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
            return "blank";
        }
    }

    @GetMapping("/excel_add/error")
    public String excelProductAddError(
            Model model,
            @Permission PermissionDto permissionDto,
            @ModelAttribute("searchDto") ProductExcelAddErrorSearchDto productExcelAddErrorSearchDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("errorList", productService.getProductExcelAddErrorList(productExcelAddErrorSearchDto));
        return "product/product/excel_add_error_list";
    }

    @GetMapping("/excel_add/error/excel_down")
    public String excelProductAddErrorDown(
            HttpServletResponse response,
            Model model,
            @Permission PermissionDto permissionDto
    ) throws IOException {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("errorList", productService.getProductExcelAddErrorExcelDown());

        String fileName = "상품엑셀입력오류_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))+".xls";
        String outputFileName = new String(fileName.getBytes("KSC5601"), "8859_1");

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.addHeader("Content-Disposition","attachment;filename=\"" + outputFileName + "\"");

        return "/product/product/excel/add_error_down";
    }
}
