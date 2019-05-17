package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.plinth.config.database.model.Configuration;
import tech.plinth.config.database.repository.ConfigurationRepository;
import tech.plinth.config.interceptor.model.RequestContext;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ConfigurationDelegate {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Resource
    private RequestContext requestContext;


    public JsonNode createNewVersion(JsonNode data) {

        Long version = calculateNextVersionNumber();

        // create the new version
        Configuration configuration = new Configuration(requestContext.getPlatformId(), data, version);

        Configuration configurationSaved = configurationRepository.save(configuration);

//        logger.info("New version of configuration service created: id: {}, version: {}, tenant: {}, data: {}",
//                configurationSaved.getId(), configuration.getVersion(),
//                configuration.getTenant(), configuration.getData());

        return configurationSaved.getData();
    }

    /**
     * define and return the next version number to be created
     * find the max version number in DB and the next version will be the max version number + 1
     *
     * @return
     */
    public Long calculateNextVersionNumber() {
        List<Configuration> allVersions = configurationRepository.findAll();

        Long version = 0L;

        if (allVersions.size() != 0) {
            // get last version inserted in DB
            version = allVersions.stream()
                    .map(configuration -> configuration.getVersion())
                    .max(Long::compare).get();
        }

//        logger.info("The last configuration created as the version: {}", version);

        version = version + 1L;
        return version;
    }


}
