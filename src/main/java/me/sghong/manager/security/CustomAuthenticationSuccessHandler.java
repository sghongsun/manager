package me.sghong.manager.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.common.service.MyMenuChoiceService;
import me.sghong.manager.app.manage.dto.AdminDto;
import me.sghong.manager.app.common.dto.MyMenuChoiceDto;
import me.sghong.manager.app.manage.service.AdminService;
import me.sghong.manager.util.CommonUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final AdminService adminService;
    private final MyMenuChoiceService myMenuChoiceService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        setDefaultTargetUrl("/");

        String id = request.getParameter("mid");

        adminService.AdminLoginSuccess(id);

        String MenuChoice = "";
        List<MyMenuChoiceDto> myMenuChoiceDtoList = myMenuChoiceService.getList(id);
        if (!myMenuChoiceDtoList.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for(MyMenuChoiceDto key : myMenuChoiceDtoList) {
                sb.append(key.getMenucode()).append(",");
            }
            MenuChoice = sb.deleteCharAt(sb.length() - 1).toString();
        }

        AdminDto adminDto = adminService.getAdminInfoForGroup(id);

        CommonUtil.setSession("adminid", id);
        CommonUtil.setSession("adminname", adminDto.getAdminname());
        CommonUtil.setSession("admingroup", Integer.toString(adminDto.getGroupcode()));
        CommonUtil.setSession("menuchoice", MenuChoice);
        CommonUtil.setSession("ip", CommonUtil.getRemoteIP());
        CommonUtil.setSession("sessionid", request.getSession().getId());

        String saveid = "";
        if (request.getParameter("saveid") != null){
            saveid = request.getParameter("saveid");
        }

        if (saveid.equals("Y")) {
            CommonUtil.setCookie("saveadminid", id, 60*60*24*365);
            CommonUtil.setCookie("saveid", saveid, 60*60*24*365);
        } else {
            CommonUtil.delCookie("saveadminid");
            CommonUtil.delCookie("saveid");
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
