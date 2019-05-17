package tech.plinth.config.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.plinth.config.interceptor.model.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ConfigureRequestContext implements HandlerInterceptor {

    public static final String HEADER_PLATFORM_ID = "M-Platform-Id";

    @Resource
    private RequestContext requestContext;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) throws Exception {

        String platformId = httpServletRequest.getHeader(HEADER_PLATFORM_ID);


        if (platformId == null || platformId.isBlank() || platformId.isEmpty()) {
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
            //TODO: log error request
            return false;
        }

        requestContext.setPlatformId(platformId);
        //TODO: log success request
        return true;
    }
}
