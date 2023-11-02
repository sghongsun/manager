package me.sghong.manager.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.app.manage.dto.AdminDto;
import me.sghong.manager.app.manage.service.AdminService;
import me.sghong.manager.util.CommonUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;

@RequiredArgsConstructor
@Component("customAuthenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final AdminService adminService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "아이디 또는 비밀번호를 확인 하여 주시기 바랍니다.";

        if (exception instanceof UsernameNotFoundException e) {
            errorMessage = e.getMessage();
        } else if (exception instanceof BadCredentialsException e) {
            String id = request.getParameter("mid");

            AdminDto adminDto = adminService.getAdminInfoForGroup(id);
            int pwderrcnt = adminDto.getPwderrcnt() + 1;

            if (pwderrcnt <= 5) {
                errorMessage = "비밀번호가 " + pwderrcnt + "번 틀렸습니다. 5번 틀리면 계정이 잠깁니다.";
                adminService.LoginForPwdErrUpdate(id);
            }

            if (pwderrcnt > 5) {
                errorMessage = "비밀번호가 5번 틀렸습니다. 관리자에게 문의 하시기 바랍니다.";
            }
            //errorMessage = e.getMessage();
        } else if (exception instanceof InsufficientAuthenticationException e) {
            errorMessage = e.getMessage();
        }

        CommonUtil.AlertMessage(errorMessage, "history.back();");

//        errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
//        setDefaultFailureUrl("/login?error=true&exception=" + errorMessage);
//        super.onAuthenticationFailure(request, response, exception);
    }

}
