package me.sghong.manager.app.manage.conrtorller;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.request.AdminGroupRequest;
import me.sghong.manager.app.manage.service.AdminGroupService;
import me.sghong.manager.app.manage.service.AdminService;
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

@RequiredArgsConstructor
@Controller
@RequestMapping("/manage/group")
public class AdminGroupController {
    private final AdminGroupService adminGroupService;
    private final MenuService menuService;
    private final AdminService adminService;

    @GetMapping("/list")
    public String groupList(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("admingroupList", adminGroupService.getList());
        return "manage/group/list";
    }

    @GetMapping("/add")
    public String groupAdd(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("depth1MenuList", menuService.getMenuDepth1ForAll());
        model.addAttribute("depth2MenuList", menuService.getMenuDepth2ForAll());
        return "manage/group/add";
    }

    @PostMapping("/add")
    public void groupAddOk(
            @Validated AdminGroupRequest adminGroupRequest
    ) throws IOException {
        adminGroupService.insertAdminGroup(adminGroupRequest);
        CommonUtil.AlertMessage("등록 되었습니다.", "location.replace('/manage/group/list');");
    }

    @GetMapping("/modify/{groupCode}")
    public String groupModify(
            Model model,
            @Permission PermissionDto permissionDto,
            @PathVariable("groupCode") int groupCode
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("depth1MenuList", menuService.getMenuDepth1ForAll());
        model.addAttribute("depth2MenuList", menuService.getMenuDepth2ForAll());
        model.addAttribute("admingroup", adminGroupService.getInfoByGroupCode(groupCode));
        return "manage/group/modify";
    }

    @PostMapping("/modify/{groupCode}")
    public void groupModifyOk(
            @PathVariable("groupCode") int groupCode,
            @Validated AdminGroupRequest adminGroupRequest
    ) throws IOException {
        adminGroupService.updateAdminGroup(groupCode, adminGroupRequest);
        CommonUtil.AlertMessage("수정 되었습니다.", "location.replace('/manage/group/list');");
    }

    @PostMapping("/delete/{groupCode}")
    public void groupDeleteOk(
            @PathVariable("groupCode") int groupCode
    ) throws IOException {
        adminGroupService.deleteAdminGroup(groupCode);
        CommonUtil.AlertMessage("삭제 되었습니다.", "location.replace('/manage/group/list');");
    }

    @GetMapping("/popup/adminlist/{groupCode}")
    public String groupInAdminList(
            Model model,
            @Permission PermissionDto permissionDto,
            @PathVariable("groupCode") int groupCode
    ) {
        model.addAttribute("groupCode", groupCode);
        model.addAttribute("admingroupList", adminGroupService.getList());
        model.addAttribute("adminList", adminService.getAdminListByGroupCode(groupCode));
        return "/manage/group/popup/adminlist";
    }
}
