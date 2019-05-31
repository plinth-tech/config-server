package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import tech.plinth.config.database.model.Configuration;
import tech.plinth.config.database.repository.ConfigurationRepository;
import tech.plinth.config.interceptor.model.RequestContext;

import javax.annotation.Resource;
import java.util.regex.Pattern;

@Component
public class ConfigurationDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationDelegate.class);

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Resource
    private RequestContext requestContext;


    public JsonNode createNewVersion(JsonNode data) {

        if (data == null) {
            logger.error("PlatformId:'' RequestId:'' Message: No data to create a new version");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No data to create a new version");
        }

        if (!validPlatform(requestContext.getPlatformId())) {
            logger.error("PlatformId:'' RequestId:'' Message: Platform Id syntax not accepted");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Platform Id syntax not accepted");
        }

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

        if (Strings.isBlank(platformId)) {
            logger.error("PlatformId:'' RequestId:'' Message: Platform identification not specified");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Platform ID not defined");
        }

        Long newVersion = configurationRepository.findTopByPlatformOrderByVersionDesc(platformId)
                .orElse(new Configuration.Builder()
                        .version(0L)
                        .build())
                .getVersion() + 1L;

        if (configurationRepository.findByPlatformAndVersion(platformId, newVersion) != null) {
            logger.error("PlatformId:{} RequestId:{} Message: This version number already assigned",
                    requestContext.getPlatformId(), requestContext.getRequestId());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "This version number (" + newVersion + ") already assigned");
        }

        return newVersion;
    }

    /**
     * Hostname FQDN validation
     */
    public Boolean validPlatform(String platform) {
        return Pattern.matches("(?=^.{2,255}$)(^((?!-)[a-zA-Z0-9-_]{1,}[a-zA-Z0-9-_]\\.)+[a-zA-Z]{2,}$)", platform);
    }


}

