package me.sghong.manager.app.manage.conrtorller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.AdminSearchDto;
import me.sghong.manager.app.manage.request.*;
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

    @PostMapping("/ajax/admingroupcodemodify")
    public void adminModifyGroupCode(
            HttpServletResponse response,
            @Validated AdminModifyGroupRequest adminModifyGroupRequest
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(adminService.updateAdminGroup(adminModifyGroupRequest));
        out.flush();
    }

    @PostMapping("/ajax/adminpwdmodify/{adminId}")
    public String adminPwdModify(
            Model model,
            @PathVariable("adminId") String adminId
    ) {
        model.addAttribute("adminId", adminId);
        return "manage/admin/ajax/adminpwdmodify";
    }

    @PostMapping("/ajax/adminpwdmodify")
    public void adminPwdModifyOk(
            HttpServletResponse response,
            @Validated AdminModifyPwdRequet adminModifyPwdRequet
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(adminService.updateAdminPwd(adminModifyPwdRequet));
        out.flush();
    }

    @PostMapping("/adminhpmodify")
    public void adminHpModifyOk(
            @Validated AdminModifyHpRequest adminModifyHpRequest
    ) throws IOException {
        String result = adminService.updateAdminHp(adminModifyHpRequest);
        if (result.equals("OK")) {
            CommonUtil.AlertMessage("핸드폰 번호가 수정 되었습니다.", "location.replace('/manage/admin/popup/admininfo/"+adminModifyHpRequest.getAdminid()+"');");
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
        }
    }

    @PostMapping("/adminmodify")
    public void adminInfoModifyOk(
            @Validated AdminModifyRequest adminModifyRequest
    ) throws IOException {
        String result = adminService.updateAdminInfo(adminModifyRequest);
        if (result.equals("OK")) {
            CommonUtil.AlertMessage("수정 되었습니다.", "location.replace('/manage/admin/popup/admininfo/"+adminModifyRequest.getAdminid()+"');");
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
        }
    }

    @PostMapping("/admindelete")
    public void adminDeleteOk(
            @Validated AdminDeleteRequest adminDeleteRequest
    ) throws IOException {
        String result = adminService.deleteAdmin(adminDeleteRequest);
        if (result.equals("OK")) {
            CommonUtil.AlertMessage("삭제 되었습니다.", "opener.reload();self.close();");
        } else {
            CommonUtil.AlertMessage(result, "history.back();");
        }
    }

    @GetMapping("/popup/loginlist/{adminId}")
    public String adminLoginList(
            Model model,
            @PathVariable("adminId") String adminId,
            @ModelAttribute("searchDto") AdminSearchDto adminSearchDto
    ) {
        if (adminSearchDto.getKeyword() == null) {
            adminSearchDto.setKeyword(adminId);
        }
        model.addAttribute("adminId",adminId);
        model.addAttribute("loginList", adminService.getAdminLoginList(adminSearchDto));
        return "manage/admin/popup/loginlist";
    }

    @GetMapping("/menuauth")
    public String adminMenuAuth(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("menuAuthList", menuService.getMenuAuthList());
        return "manage/admin/menuauth";
    }

    @GetMapping("/popup/authgrouplist")
    public String menuAuthGroupList(
            Model model,
            @Permission PermissionDto permissionDto,
            @RequestParam("MCode1") String mCode1,
            @RequestParam("MCode2") String mCode2,
            @RequestParam("authType") String authType
    ) {
        model.addAttribute("mCode1", mCode1);
        model.addAttribute("mCode2", mCode2);
        model.addAttribute("authType", authType);
        model.addAttribute("menuDepth1List", menuService.getMenuDepth1ForUse());
        model.addAttribute("menuDepth2List", menuService.getMenuDepth2ForUse(mCode1));
        model.addAttribute("groupList", adminGroupService.getListForAdminList(mCode2, authType));

        return "manage/admin/popup/authgrouplist";
    }

    @GetMapping("/popup/authadminlist")
    public String menuAuthAdminList(
            Model model,
            @Permission PermissionDto permissionDto,
            @RequestParam("MCode1") String mCode1,
            @RequestParam("MCode2") String mCode2,
            @RequestParam("authType") String authType
    ) {
        model.addAttribute("mCode1", mCode1);
        model.addAttribute("mCode2", mCode2);
        model.addAttribute("authType", authType);
        model.addAttribute("menuDepth1List", menuService.getMenuDepth1ForUse());
        model.addAttribute("menuDepth2List", menuService.getMenuDepth2ForUse(mCode1));
        model.addAttribute("adminList", adminService.getListForGroupSearch(mCode2, authType));

        return "manage/admin/popup/authadminlist";
    }
}
