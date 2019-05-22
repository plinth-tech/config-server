package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.plinth.config.database.model.Configuration;
import tech.plinth.config.database.repository.ConfigurationRepository;
import tech.plinth.config.interceptor.model.RequestContext;

import javax.annotation.Resource;

@Component
public class ConfigurationDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationDelegate.class);

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Resource
    private RequestContext requestContext;


    public JsonNode createNewVersion(JsonNode data) {

        Long version = calculateNextVersionNumber(requestContext.getPlatformId());

        Configuration configuration = new Configuration(requestContext.getPlatformId(), data, version);
        Configuration configurationSaved = configurationRepository.save(configuration);

        logger.debug("PlatformId:{} RequestId:{} Message: New version of configuration service created",
                requestContext.getPlatformId(), requestContext.getRequestId());

        return configurationSaved.getDataJson();
    }

    /**
     * define and return the next version number to be created
     * find the max version number in DB and the next version will be the that version number + 1
     */
    public Long calculateNextVersionNumber(String platformId) {
        return configurationRepository.findTopByPlatformOrderByVersionDesc(platformId)
                .orElse(new Configuration.Builder()
                        .version(0L)
                        .build())
                .getVersion() + 1L;
    }

}