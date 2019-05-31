package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tech.plinth.config.database.model.Configuration;
import tech.plinth.config.database.repository.BaseRepository;
import tech.plinth.config.database.repository.ConfigurationRepository;
import tech.plinth.config.interceptor.model.RequestContext;

import javax.annotation.Resource;

@Component
public class ConfigurationDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationDelegate.class);

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private BaseRepository baseRepository;

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
     * return specific version merged with base configuration from that platform
     */
    public JsonNode getVersion(Long version) throws JsonPatchException {
        if (version == null) {
            logger.error("Platform:{} RequestId:{} Message: Version not specified",
                    requestContext.getPlatformId(), requestContext.getRequestId());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Version not specified");
        }

        JsonNode jsonNodeVersion = configurationRepository.findByPlatformAndVersion(requestContext.getPlatformId(), version)
                .orElseThrow(() -> {
                    logger.error("Platform:{} RequestId:{} Message: Configuration to this version not found",
                            requestContext.getPlatformId(), requestContext.getRequestId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Configuration to this version not found");
                }).getDataJson();

        return mergeConfigurationWithBase(jsonNodeVersion);
    }

    /**
     * merge the configuration version coming in parameter with base configuration and return it
     */
    public JsonNode mergeConfigurationWithBase(JsonNode jsonNodeVersion) throws JsonPatchException {
        JsonNode jsonNodeBase = baseRepository.findTopByOrderByVersionDesc()
                .orElseThrow(() -> {
                    logger.error("Platform:{} RequestId:{} Message: No Base configuration found");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No Base configuration found");
                }).getDataJson();

        JsonMergePatch patch = JsonMergePatch.fromJson(jsonNodeVersion);

        JsonNode jsonMerged = patch.apply(jsonNodeBase);

        return jsonMerged;
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