package me.sghong.manager.app.manage.conrtorller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.AdminSearchDto;
import me.sghong.manager.app.manage.request.AdminAddRequest;
import me.sghong.manager.app.manage.service.AdminGroupService;
import me.sghong.manager.app.manage.service.AdminService;
import me.sghong.manager.app.manage.service.MenuService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Controller
@RequestMapping("/manage/admin")
public class AdminController {
    private final AdminService adminService;
    private final AdminGroupService adminGroupService;
    private final MenuService menuService;

    @GetMapping("/list")
    public String getList(
            Model model,
            @Permission PermissionDto permissionDto,
            @ModelAttribute("searchDto") AdminSearchDto searchDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("groupList", adminGroupService.getList());
        model.addAttribute("adminList", adminService.getList(searchDto));
        return "manage/admin/list";
    }

    @GetMapping("/add")
    public String adminAdd(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("groupList", adminGroupService.getList());
        return "manage/admin/add";
    }

    @PostMapping("/ajax/adminidduplicatecheck")
    public void adminIdDuplicateCheck(
            HttpServletResponse response,
            @RequestParam("AdminID") String adminId
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(adminService.getAdminIdDuplicate(adminId) + "|||||");
        out.flush();
    }

    @PostMapping("/add")
    public void adminAddOk(
            @Validated AdminAddRequest adminAddRequest
    ) throws IOException {
        if (!adminAddRequest.getPwd().equals(adminAddRequest.getPwd1())) {
            CommonUtil.AlertMessage("비밀번호가 일치 하지 않습니다.", "history.back();");
        }
        adminService.insertAdmin(adminAddRequest);
        CommonUtil.AlertMessage("등록 되었습니다.", "location.replace('/manage/admin/list');");
    }

    @GetMapping("/popup/admininfo/{adminId}")
    public String adminInfo(
            Model model,
            @Permission PermissionDto permissionDto,
            @PathVariable("adminId") String adminId
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("groupList", adminGroupService.getList());
        model.addAttribute("admin", adminService.getAdminInfo(adminId));
        return "manage/admin/popup/admininfo";
    }

}
