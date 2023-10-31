package me.sghong.manager.app.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.dto.FileDto;
import me.sghong.manager.util.CommonUtil;
import me.sghong.manager.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class CommonController {
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

        return "main/editorfileupload";
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
}
