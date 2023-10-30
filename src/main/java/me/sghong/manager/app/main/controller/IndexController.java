package me.sghong.manager.app.main.controller;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.service.MenuService;
import me.sghong.manager.resolver.Permission;
import me.sghong.manager.security.AuthPermission;
import me.sghong.manager.security.AuthSecurity;
import me.sghong.manager.security.dto.PermissionDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final AuthPermission authPermission;

    @GetMapping("/")
    public String index(
            Model model,
            @Permission PermissionDto permissionDto
            ) throws IOException {
        model.addAttribute("Permission", permissionDto);
        //model.addAttribute("topMenu", authPermission.getTopMenu(permissionDto.getMenuRead()));
        System.out.println("permission:" + permissionDto);
        return "main/index";
    }
}
