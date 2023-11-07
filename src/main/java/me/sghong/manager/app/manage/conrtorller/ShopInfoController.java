package me.sghong.manager.app.manage.conrtorller;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.request.ShopInfoRequest;
import me.sghong.manager.app.manage.service.ShopInfoService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manage/shopinfo")
public class ShopInfoController {
    private final ShopInfoService shopInfoService;

    @GetMapping("/info")
    public String shopInfo(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("shopinfo", shopInfoService.getShopInfo());
        return "manage/shopinfo/shopinfo";
    }

    @PostMapping("/modify")
    public void shopInfoModifyOk(
            @Validated ShopInfoRequest shopInfoRequest
    ) throws IOException {
        shopInfoService.updateShopInfo(shopInfoRequest);
        CommonUtil.AlertMessage("수정 되었습니다.", "location.replace('/manage/shopinfo/info');");
    }
}
