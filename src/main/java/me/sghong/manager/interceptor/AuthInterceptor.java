package me.sghong.manager.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.sghong.manager.security.AuthPermission;
import me.sghong.manager.security.AuthSecurity;
import me.sghong.manager.util.CommonUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
@AuthSecurity
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthPermission authPermission;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        if (!CommonUtil.getRemoteIP().equals(CommonUtil.getSession("ip"))) {
            CommonUtil.AlertMessage("정상적인 접근이 아닙니다.", "location.replace('/login');");
            return false;
        }

        if (request.getMethod().equals("POST")) {
            return authPermission.AuthPostCheck();
        }

        return true;
    }
}
