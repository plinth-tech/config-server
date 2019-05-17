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
import java.util.List;

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

        logger.debug("PlatformId:{} RequestId:{} Message: New version of configuration service created: id: {}, version: {}, tenant: {}, data: {}",
                requestContext.getPlatformId(), requestContext.getRequestId(),
                configurationSaved.getId(), configuration.getVersion(),
                configuration.getTenant(), configuration.getDataJson());

        return configurationSaved.getDataJson();
    }

    /**
     * define and return the next version number to be created
     * find the max version number in DB and the next version will be the max version number + 1
     *
     * @return
     */
    public Long calculateNextVersionNumber(String platformId) {

        List<Configuration> allVersions = configurationRepository.findByTenant(platformId);
        logger.debug("PlatformId:{} RequestId:{} Message: Number of versions created so far by {}: {}",
                requestContext.getPlatformId(), requestContext.getRequestId(), platformId, allVersions.size());

        Long version = 0L;

        if (allVersions.size() != 0) {
            // get last version inserted in DB
            version = allVersions.stream()
                    .map(configuration -> configuration.getVersion())
                    .max(Long::compare).get();
        }

        logger.debug("PlatformId:{} RequestId:{} Message: The last configuration created of {} as the version: {}",
                requestContext.getPlatformId(), requestContext.getRequestId(), platformId, version);

        version = version + 1L;
        return version;
    }


}
