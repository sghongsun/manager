package me.sghong.manager.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class HTMLTagFilter implements Filter {
    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        /*필터 제외 url*/
        List<String> excludeUrls = new ArrayList<>();
        excludeUrls.add("/manage/terms/add");
        excludeUrls.add("/manage/terms/addok");
        excludeUrls.add("/manage/terms/modify");
        excludeUrls.add("/manage/terms/modifyok");
        excludeUrls.add("/product/product/add");
        excludeUrls.add("/product/product/addok");
        excludeUrls.add("/product/product/modify");
        excludeUrls.add("/product/product/modifyok");

        String path = ((HttpServletRequest) request).getRequestURI();
        boolean matching = false;
        for (String exurl : excludeUrls) {
            if (path.contains(exurl)) {
                matching = true;
                break;
            }
        }

        if (matching) {
            chain.doFilter(request, response);
        } else {
            chain.doFilter(new HTMLTagFilterRequestWrapper((HttpServletRequest)request), response);
        }
    }

    @Override
    public void destroy() {

    }
}
