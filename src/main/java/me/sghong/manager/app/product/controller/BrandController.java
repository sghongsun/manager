package me.sghong.manager.app.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.MessageDto;
import me.sghong.manager.app.product.dto.BrandSearchDto;
import me.sghong.manager.app.product.request.BrandAddRequest;
import me.sghong.manager.app.product.request.BrandDisplayNumModifyRequest;
import me.sghong.manager.app.product.request.BrandModifyRequest;
import me.sghong.manager.app.product.request.BrandUseFlagModifyRequest;
import me.sghong.manager.app.product.service.BrandService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@RequiredArgsConstructor
@Controller
@RequestMapping("/product/brand")
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/list")
    public String brandList(
            Model model,
            @Permission PermissionDto permissionDto,
            @ModelAttribute("searchDto") BrandSearchDto brandSearchDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("brandList", brandService.getList(brandSearchDto));
        return "product/brand/list";
    }

    @GetMapping("/add")
    public String brandAdd(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        return "product/brand/add";
    }

    @PostMapping("/add")
    public void brandAddOk(
            @Validated BrandAddRequest brandAddRequest
    ) throws IOException {
        String result = brandService.insertBrand(brandAddRequest);
        if (result.equals("OK")) {
            CommonUtil.AlertMessage("등록 되었습니다.", "location.replace('/product/brand/list');");
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
        }
    }

    @GetMapping("/modify/{brandCode}")
    public String brandInfo(
            Model model,
            @Permission PermissionDto permissionDto,
            @PathVariable("brandCode") String BrandCode,
            @ModelAttribute("searchDto") BrandSearchDto brandSearchDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("brand", brandService.getInfo(BrandCode));
        return "product/brand/modify";
    }

    @PostMapping("/modify")
    public String brandModifyOk(
            Model model,
            @Validated BrandModifyRequest brandModifyRequest,
            BrandSearchDto brandSearchDto
    ) throws IOException {
        String result = brandService.updateBrand(brandModifyRequest);

        if (result.equals("OK")) {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Object> map = objectMapper.convertValue(brandSearchDto, HashMap.class);

            MessageDto messageDto = MessageDto.builder()
                    .message("수정 되었습니다.")
                    .redirectUri("/product/brand/list")
                    .method(RequestMethod.GET)
                    .data(map)
                    .build();

            model.addAttribute("params", messageDto);
            return "common/messageRedirect";
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
            return null;
        }
    }

    @PostMapping("/ajax/useflag_modify")
    public void brandUseFlagModifyOk(
            HttpServletResponse response,
            @Validated BrandUseFlagModifyRequest brandUseFlagModifyRequest
    ) throws IOException {
        String result = brandService.updateBrandUseFlag(brandUseFlagModifyRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        if (result.equals("OK")) {
            out.println("OK|||||");
        } else {
            out.println("FAIL|||||"+result);
        }
        out.flush();
    }

    @GetMapping("/popup/displaylist")
    public String brandDisplayList(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("brandList", brandService.getListAll());
        return "product/brand/popup/displaylist";
    }

    @PostMapping("/ajax/displaynummodify")
    public void brandDisplayNumModifyOk(
            HttpServletResponse response,
            @Validated BrandDisplayNumModifyRequest brandDisplayNumModifyRequest
    ) throws IOException {
        brandService.updateBrandDisplayNum(brandDisplayNumModifyRequest);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("OK|||||");
        out.flush();
    }
}
