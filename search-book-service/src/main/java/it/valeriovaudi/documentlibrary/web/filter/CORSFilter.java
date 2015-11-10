package it.valeriovaudi.documentlibrary.web.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Valerio on 13/05/2015.
 */

@Component
public class CORSFilter implements Filter {
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN_NAME = "Access-Control-Allow-Origin";
    public static final String DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN_VALUE = "*";

    public static final String ACCESS_CONTROL_ALLOW_METHDOS_NAME = "Access-Control-Allow-Methods";
    public static final String DEFAULT_ACCESS_CONTROL_ALLOW_METHDOS_VALUE = "POST, GET, OPTIONS, DELETE";

    public static final String ACCESS_CONTROL_MAX_AGE_NAME = "Access-Control-Max-Age";
    public static final String DEFAULT_ACCESS_CONTROL_MAX_AGE_VALUE = "3600";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS_NAME = "Access-Control-Allow-Headers";
    public static final String DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS_VALUE = "x-requested-with";

    private String accessControlAllowOrigin = DEFAULT_ACCESS_CONTROL_ALLOW_ORIGIN_VALUE;
    private String accessControlAllowMethods = DEFAULT_ACCESS_CONTROL_ALLOW_METHDOS_VALUE;
    private String accessControlAllowMaxAge = DEFAULT_ACCESS_CONTROL_MAX_AGE_VALUE;
    private String accessControlAllowHeaders = DEFAULT_ACCESS_CONTROL_ALLOW_HEADERS_VALUE;

    /**
     * @return the method return a map that associated the name of paramiters in the web.xml to the class variable name for the header binding
     */
    private Map<String, String> initConfig() {
        Map<String, String> result = new HashMap<>();

        result.put(ACCESS_CONTROL_ALLOW_ORIGIN_NAME, "accessControlAllowOrigin");
        result.put(ACCESS_CONTROL_ALLOW_METHDOS_NAME, "accessControlAllowMethods");
        result.put(ACCESS_CONTROL_MAX_AGE_NAME, "accessControlAllowMaxAge");
        result.put(ACCESS_CONTROL_ALLOW_HEADERS_NAME, "accessControlAllowHeaders");

        return result;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String initParameterValue;
        Map<String, String> stringStringMap = initConfig();

        for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
            initParameterValue = filterConfig.getInitParameter(stringStringEntry.getKey());

            // if the init paramiter value isn't null then set the value in the correct http header
            if (initParameterValue != null) {
                try {
                    getClass().getDeclaredField(stringStringEntry.getValue()).set(this, initParameterValue);
                } catch (IllegalAccessException | NoSuchFieldException ignored) {
                }
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_NAME, accessControlAllowOrigin);
        response.setHeader(ACCESS_CONTROL_ALLOW_METHDOS_NAME, accessControlAllowMethods);
        response.setHeader(ACCESS_CONTROL_MAX_AGE_NAME, accessControlAllowMaxAge);
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_NAME, accessControlAllowHeaders);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}