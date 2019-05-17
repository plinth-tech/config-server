package tech.plinth.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.plinth.config.delegate.ConfigurationDelegate;
import tech.plinth.config.interceptor.model.RequestContext;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class ConfigureRequestContext implements HandlerInterceptor {

    public static final String HEADER_PLATFORM_ID = "M-Platform-Id";

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationDelegate.class);

    @Resource
    private RequestContext requestContext;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse,
                             Object o) throws Exception {

        String platformId = httpServletRequest.getHeader(HEADER_PLATFORM_ID);

        if (platformId == null || platformId.isBlank() || platformId.isEmpty()) {
            httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value());
            logger.error("PlatformId:{} RequestId:'' Message: Platform identification not specified",
                    platformId);
            return false;
        }

        requestContext.setPlatformId(platformId);
        requestContext.setRequestId(UUID.randomUUID().toString());
        logger.debug("PlatformId:{} RequestId:{} Message: Request id created for this platform",
                requestContext.getPlatformId(), requestContext.getRequestId());
        return true;
    }
}
