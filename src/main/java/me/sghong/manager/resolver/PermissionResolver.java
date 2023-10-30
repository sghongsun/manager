package me.sghong.manager.resolver;

import lombok.RequiredArgsConstructor;
import me.sghong.manager.security.AuthPermission;
import me.sghong.manager.security.AuthSecurity;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
@AuthSecurity
public class PermissionResolver implements HandlerMethodArgumentResolver {
    private final AuthPermission authPermission;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Permission.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        return authPermission.getPermission();
    }
}
