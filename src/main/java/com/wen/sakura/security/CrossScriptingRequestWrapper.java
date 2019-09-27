package com.wen.sakura.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author huwenwen
 * @date 2019/9/27
 */
public class CrossScriptingRequestWrapper extends HttpServletRequestWrapper {

    public CrossScriptingRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(cleanXSS(name));
        if (value != null) {
            value = cleanXSS(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(cleanXSS(name));
        if (value != null) {
            value = cleanXSS(value);
        }
        return value;
    }

    private String cleanXSS(String value) {
        //特殊字符转成全角
        String result = value.replaceAll("&", "＆");
        result = result.replaceAll("<", "＜").replaceAll(">", "＞");
        result = result.replaceAll("\\(", "\\（").replaceAll("\\)", "\\）");
        result = result.replaceAll("'", "＇");
        result = result.replaceAll("eval\\((.*)\\)", "");
        result = result.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        result = result.replaceAll("script", "");
        return result;
    }
}
