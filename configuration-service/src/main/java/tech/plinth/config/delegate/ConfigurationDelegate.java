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

        logger.debug("PlatformId:{} RequestId:{} Message: New version of configuration service created: id: {}, version: {}, platform: {}, data: {}",
                requestContext.getPlatformId(), requestContext.getRequestId(),
                configurationSaved.getId(), configuration.getVersion(),
                configuration.getPlatform(), configuration.getDataJson());
        return configurationSaved.getDataJson();
    }

    /**
     * define and return the next version number to be created
     * find the max version number in DB and the next version will be the that version number + 1
     *
     * @return
     */
    public Long calculateNextVersionNumber(String platformId) {

        Configuration lastVersion = configurationRepository.findTopByPlatformOrderByVersionDesc(platformId);

        Long newVersion = 0L;

        if (lastVersion != null) {
            newVersion = lastVersion.getVersion() + 1L;
            logger.debug("PlatformId:{} RequestId:{} Message: The last configuration created of {} as the version: {}",
                    requestContext.getPlatformId(), requestContext.getRequestId(), platformId, lastVersion.getVersion());
        }

        logger.debug("PlatformId:{} RequestId:{} Message: First version for platform {}",
                requestContext.getPlatformId(), requestContext.getRequestId(), platformId);

        return newVersion;
    }


}
