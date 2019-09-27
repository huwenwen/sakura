package com.wen.sakura.security;

import com.wen.sakura.util.Helper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
public class XSSFilter implements Filter {

    private List<String> excludeList;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            filterChain.doFilter(request, response);
            return;
        }
        String url = ((HttpServletRequest) request).getServletPath();
        if (Helper.isEmpty(excludeList) || !excludeList.contains(url)) {
            CrossScriptingRequestWrapper csRequest = new CrossScriptingRequestWrapper((HttpServletRequest) request);
            filterChain.doFilter(csRequest, response);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
