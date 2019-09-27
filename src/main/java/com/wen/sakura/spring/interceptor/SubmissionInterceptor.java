package com.wen.sakura.spring.interceptor;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * preHandler 返回false不会继续执行
 * postHandler 只有在pre返回true执行,抛异常和false都不执行
 * afterCompletion preHandler返回false或者抛异常执行前面执行过的拦截器的afterCompletion(不包含本次)
 * 常用于资源清理
 *
 * @author huwenwen
 * @date 2019/9/27
 */
public class SubmissionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }
        if (!lock(getKey(request))) {
            response.setStatus(HttpStatus.LOCKED.value());
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return;
        }
        unlock(getKey(request));
    }

    private String getKey(HttpServletRequest request) {
        return request.getRequestURI();
    }

    private boolean lock(String key) {
        // redis setnx
        return true;
    }

    private void unlock(String key) {

    }
}
