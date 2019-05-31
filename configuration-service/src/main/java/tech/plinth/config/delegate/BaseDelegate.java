package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tech.plinth.config.database.repository.BaseRepository;
import tech.plinth.config.interceptor.model.RequestContext;

import javax.annotation.Resource;

@Component
public class BaseDelegate {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationDelegate.class);

    @Autowired
    private BaseRepository baseRepository;

    @Resource
    private RequestContext requestContext;

    /**
     * if exist, return the newest Base configuration
     */
    public JsonNode getBase() {
        return baseRepository.findTopByOrderByVersionDesc().orElseThrow(() -> {
            logger.error("PlatformId:{} RequestId:{} Message: Base configuration not found",
                    requestContext.getPlatformId(), requestContext.getRequestId());
            return new ResponseStatusException(HttpStatus.NOT_FOUND);
        }).getDataJson();
    }
}
