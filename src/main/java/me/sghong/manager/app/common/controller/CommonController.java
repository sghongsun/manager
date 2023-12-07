package me.sghong.manager.app.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.FileDto;
import me.sghong.manager.app.common.request.MyMenuChoiceAddDeleteRequest;
import me.sghong.manager.app.common.request.MyMenuChoiceDispNumUpdateRequest;
import me.sghong.manager.app.common.service.MyMenuChoiceService;
import me.sghong.manager.app.product.dto.CategoryDto;
import me.sghong.manager.app.product.service.CategoryService;
import me.sghong.manager.util.CommonUtil;
import me.sghong.manager.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Controller
public class CommonController {
    private final MyMenuChoiceService myMenuChoiceService;
    private final CategoryService categoryService;

    @GetMapping("/login")
    public String login(
            Model model
    ) {
        model.addAttribute("saveid", CommonUtil.getCookie("saveid"));
        model.addAttribute("saveadminid", CommonUtil.getCookie("saveadminid"));

        return "login/login";
    }

    @GetMapping("/common/editorfileupload")
    public String editorfileupload(
            @RequestParam String I1,
            @RequestParam String DetailFolder,
            Model model
    ) {
        model.addAttribute("ID", I1);
        model.addAttribute("DetailFolder", DetailFolder);

        return "common/editorfileupload";
    }

    @PostMapping("/common/editorfileupload")
    public void editorfileuploadok(
            @RequestParam("UpdateImage") MultipartFile file,
            HttpServletRequest request
    ) throws IOException {
        if (!FileUtil.validIFileMimetype(file, "image")) {
            CommonUtil.AlertMessage("이미지 파일만 등록해 주세요.", "history.back();");
        }

        String uploadfoler = "editor";
        if (request.getParameter("DetailFolder") != null && ! request.getParameter("DetailFolder").isEmpty()) {
            uploadfoler += "/" + request.getParameter("DetailFolder");
        }

        FileDto fileDto = FileUtil.uploadFile(file, uploadfoler);

        String retVal = "";
        retVal += "parent.parent.insertIMG('" + request.getParameter("ID") + "','" + fileDto.getSaveFileName() + "');";
        retVal += "parent.parent.oEditors.getById['" + request.getParameter("ID") + "'].exec('SE_TOGGLE_IMAGEUPLOAD_LAYER');";

        CommonUtil.AlertMessage("", retVal);
    }

    @PostMapping("/common/ajax/mymenuadd")
    public void myMenuAddOk(
            HttpServletResponse response,
            @Validated MyMenuChoiceAddDeleteRequest myMenuChoiceAddDeleteRequest
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(myMenuChoiceService.insertMyMenu(myMenuChoiceAddDeleteRequest));
    }

    @PostMapping("/common/ajax/mymnulist")
    public String myMenuList(Model model) {
        model.addAttribute("menuchoicelist", myMenuChoiceService.getList(CommonUtil.getSession("adminid")));

        return "common/mymenu/ajax/menuchoicelist";
    }

    @PostMapping("/common/ajax/mymenudisplaynummodify")
    public void myMenuDispNumUpdate(
            HttpServletResponse response,
            @Validated MyMenuChoiceDispNumUpdateRequest myMenuChoiceDispNumUpdateRequest
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(myMenuChoiceService.updateMyMenuDipsNum(myMenuChoiceDispNumUpdateRequest));
    }

    @PostMapping("/common/ajax/mymenudelete")
    public void myMenuDelete(
            HttpServletResponse response,
            @Validated MyMenuChoiceAddDeleteRequest myMenuChoiceAddDeleteRequest
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(myMenuChoiceService.deleteMyMenu(myMenuChoiceAddDeleteRequest));
    }

    @GetMapping("/common/ajax/category2list")
    public void Category2List(
            HttpServletResponse response,
            @RequestParam String categorycode1
    ) throws IOException {
        StringBuilder Code = new StringBuilder();
        StringBuilder Name = new StringBuilder();

        for (CategoryDto categoryDto : categoryService.getCategory2List(categorycode1)) {
            Code.append(categoryDto.getCategorycode2()).append(",");
            Name.append(categoryDto.getCategoryname2()).append(",");
        }

        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        if (Code.isEmpty()) {
            out.println("OK||||||||||");
        } else {
            out.println("OK|||||" + Code.deleteCharAt(Code.length() - 1).toString() + "|||||" + Name.deleteCharAt(Name.length() - 1).toString());
        }
        out.flush();
    }
}
