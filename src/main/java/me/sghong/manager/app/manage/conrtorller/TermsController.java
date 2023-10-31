package me.sghong.manager.app.manage.conrtorller;

import groovyjarjarpicocli.CommandLine;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.TermsSearchDto;
import me.sghong.manager.app.manage.request.TermsAddRequest;
import me.sghong.manager.app.manage.request.TermsDeleteRequest;
import me.sghong.manager.app.manage.request.TermsModifyRequest;
import me.sghong.manager.app.manage.service.TermsService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.dto.PermissionDto;
import me.sghong.manager.util.CommonUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping(value = "/manage/terms")
public class TermsController {
    private final TermsService termsService;
    @GetMapping("/list")
    public String termsList(
            Model model,
            @Permission PermissionDto permissionDto,
            @ModelAttribute("searchDto")TermsSearchDto termsSearchDto
    ) {
        model.addAttribute("Permission", permissionDto);

        model.addAttribute("termsList", termsService.getList(termsSearchDto));

        return "manage/terms/list";
    }

    @GetMapping("/add")
    public String termsAdd(
            Model model,
            @Permission PermissionDto permissionDto
    ) {
        model.addAttribute("Permission", permissionDto);

        return "manage/terms/add";
    }

    @PostMapping("/add")
    public void termsAddOk(
            @Validated TermsAddRequest termsAddRequest
    ) throws IOException {
        termsService.insertTerms(termsAddRequest);

        CommonUtil.AlertMessage("등록 되었습니다.", "location.replace('/manage/terms/list');");
    }

    @GetMapping("/modify/{idx}")
    public String termsModify(
            Model model,
            @Permission PermissionDto permissionDto,
            @PathVariable("idx") int idx,
            @ModelAttribute("searchDto") TermsSearchDto termsSearchDto
    ) {
        model.addAttribute("Permission", permissionDto);
        model.addAttribute("terms", termsService.getTermsInfo(idx));
        return "manage/terms/modify";
    }

    @PostMapping("/modify")
    public String termsModifyOk(
            TermsModifyRequest termsModifyRequest,
            TermsAddRequest termsAddRequest,
            TermsSearchDto termsSearchDto,
            RedirectAttributes rt
    ) throws IOException {
        termsService.updateTerms(termsModifyRequest, termsAddRequest);
        //CommonUtil.AlertMessage("수정 되었습니다.", "");
        rt.addFlashAttribute("searchDto", termsSearchDto);
        return "redirect:/manage/terms/list";
    }

    @PostMapping("/delete")
    public String termsDeleteOk(
            @Validated TermsDeleteRequest termsDeleteRequest,
            TermsSearchDto termsSearchDto,
            RedirectAttributes rt
    ) throws IOException {
        termsService.deleteTerms(termsDeleteRequest);
        rt.addFlashAttribute("searchDto", termsSearchDto);
        //CommonUtil.AlertMessage("삭제 되었습니다.", "");
        return "redirect:/manage/terms/list";
    }
}
