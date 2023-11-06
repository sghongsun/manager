package me.sghong.manager.app.manage.conrtorller;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.request.AdminModifyHpRequest;
import me.sghong.manager.app.manage.service.AdminGroupService;
import me.sghong.manager.app.manage.service.AdminService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manage/myinfo")
public class MyInfoController {
    private final AdminService adminService;
    private final AdminGroupService adminGroupService;

    @GetMapping("/modify")
    public String myInfoModify(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("admin", adminService.getAdminInfo(CommonUtil.getSession("adminid")));
        model.addAttribute("groupname",adminGroupService.getInfoByGroupCode(Integer.parseInt(CommonUtil.getSession("admingroup"))).getGroupname());
        return "manage/admin/myinfo";
    }

    @PostMapping("/ajax/mypasswordmodify")
    public String myPasswordModify(Model model) {
        model.addAttribute("adminId", CommonUtil.getSession("adminid"));
        return "manage/admin/ajax/adminpwdmodify";
    }

    @PostMapping("/myinfohpmodify")
    public void myHpModifyOk(
            @Validated AdminModifyHpRequest adminModifyHpRequest
    ) throws IOException {
        String result = adminService.updateAdminHp(adminModifyHpRequest);
        if (result.equals("OK")) {
            CommonUtil.AlertMessage("핸드폰 번호가 수정 되었습니다.", "location.replace('/manage/myinfo/modify');");
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
        }
    }
}
