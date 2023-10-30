package me.sghong.manager.app.manage.conrtorller;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.MenuDto;
import me.sghong.manager.app.manage.request.MenuAddRequest;
import me.sghong.manager.app.manage.request.MenuDeleteRequest;
import me.sghong.manager.app.manage.request.MenuDispNumModifyRequest;
import me.sghong.manager.app.manage.request.MenuModifyRequest;
import me.sghong.manager.app.manage.service.MenuService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/manage/menu")
public class MenuController {
    private final MenuService menuService;

    @GetMapping(value = {"/list", "/list/{PCode}"})
    public String menuList(
            Model model,
            @Permission PermissionDto permissionDto,
            @PathVariable(required = false) String PCode
    ) {
        model.addAttribute("Permission", permissionDto);

        List<MenuDto> menuDepth1List = menuService.getMenuDepth1ForAll();
        model.addAttribute("menuDepth1List", menuDepth1List);
        if (PCode == null && !menuDepth1List.isEmpty()) {
            PCode = menuDepth1List.get(0).getMenucode();
        }
        model.addAttribute("menuDepth2List", menuService.getMenuDepth2ForAll(PCode));
        model.addAttribute("PCode", PCode);

        return "manage/menu/list";
    }

    @PostMapping("/add")
    public void menuAddOk(
            @Validated MenuAddRequest menuAddRequest
    ) throws IOException {
        if (!menuAddRequest.getMenuPCode().equals("0000") && menuAddRequest.getMenuUrl().charAt(0) != '/') {
            CommonUtil.AlertMessage("URL 경로는 `/` 로 시작되어야 합니다.", "history.back();");
            return;
        }
        String retVal = menuService.addMenu(menuAddRequest);
        if (retVal.equals("OK")) {
            CommonUtil.AlertMessage("추가 되었습니다.", "location.replace('/manage/menu/list');");
        } else {
            CommonUtil.AlertMessage(retVal, "history.back();");
        }
    }

    @GetMapping("/modify/{menuPCode}/{menuCode}")
    public String menuModify(
            Model model,
            @Permission PermissionDto permissionDto,
            @PathVariable String menuPCode,
            @PathVariable String menuCode
    ) {
        model.addAttribute("Permission", permissionDto);

        model.addAttribute("menuDto", menuService.getMenuInfo(menuPCode, menuCode));

        return "manage/menu/modify";
    }

    @PostMapping("/modify")
    public void menuModifyOk(
            @Validated MenuModifyRequest menuModifyRequest
    ) throws IOException {
        if (!menuModifyRequest.getMenuPCode().equals("0000") && menuModifyRequest.getMenuUrl().charAt(0) != '/') {
            CommonUtil.AlertMessage("URL 경로는 `/` 로 시작되어야 합니다.", "history.back();");
            return;
        }
        String retVal = menuService.modifyMenu(menuModifyRequest);
        if (retVal.equals("OK")) {
            CommonUtil.AlertMessage("수정 되었습니다.", "location.replace('/manage/menu/modify/"+menuModifyRequest.getMenuPCode()+"/"+menuModifyRequest.getMenuCode()+"');");
        } else {
            CommonUtil.AlertMessage(retVal, "history.back();");
        }
    }

    @PostMapping("/delete")
    public void menuDeleteOk(
            @Validated MenuDeleteRequest menuDeleteRequest
    ) throws IOException {
        String retVal = menuService.deleteMenu(menuDeleteRequest);
        if (retVal.equals("OK")) {
            String returUrl = "/manage/menu/list";
            if (!menuDeleteRequest.getMenuPCode().equals("0000")) {
                returUrl += "/" + menuDeleteRequest.getMenuPCode();
            }
            CommonUtil.AlertMessage("삭제 되었습니다.", "location.replace('" + returUrl + "');");
        } else {
            CommonUtil.AlertMessage(retVal, "history.back();");
        }
    }

    @PostMapping("/dispnummodify")
    public void menuDisplayNumModify(
            @Validated MenuDispNumModifyRequest menuDispNumModifyRequest
    ) throws IOException {
        String retVal = menuService.modifyDisplayNum(menuDispNumModifyRequest);
        if (retVal.equals("OK")) {
            String returUrl = "/manage/menu/list";
            if (!menuDispNumModifyRequest.getMenuPCode().equals("0000")) {
                returUrl += "/" + menuDispNumModifyRequest.getMenuPCode();
            }
            CommonUtil.AlertMessage("", "location.replace('" + returUrl + "');");
        } else {
            CommonUtil.AlertMessage(retVal, "history.back();");
        }
    }

}
