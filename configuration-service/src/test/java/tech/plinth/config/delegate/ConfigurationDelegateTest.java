package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import tech.plinth.config.database.model.Base;
import tech.plinth.config.database.model.Configuration;
import tech.plinth.config.database.repository.BaseRepository;
import tech.plinth.config.database.repository.ConfigurationRepository;
import tech.plinth.config.interceptor.model.RequestContext;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
@SpringBootTest
public class ConfigurationDelegateTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    @Mock
    private BaseRepository baseRepository;

    @Mock
    private RequestContext requestContext;

    @InjectMocks
    private ConfigurationDelegate configurationDelegate;

    private Long baseVersion;
    private JsonNode baseJsonNode;

    private String configurationPlatform;
    private Long configurationVersion;
    private JsonNode configurationJsonNode;

    private ObjectMapper mapper;

    private JsonMergePatch patch;
    private JsonNode jsonMerged;

    @BeforeEach
    public void setUp() {
        baseVersion = 1L;
        configurationPlatform = "configuration.plinth.com";
        configurationVersion = 40L;
        mapper = new ObjectMapper();

        when(requestContext.getPlatformId()).thenReturn(configurationPlatform);

    }

    @Test
    public void newConfigurationEqualsBase() throws IOException, JsonPatchException {

        baseJsonNode = mapper.readTree("{\"config1\":\"config1\"}");
        Base base = new Base(baseVersion, baseJsonNode);

        configurationJsonNode = mapper.readTree("{\"config1\":\"config1\"}");
        Configuration configuration = new Configuration(configurationPlatform, configurationJsonNode, configurationVersion);

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(configurationPlatform)).thenReturn(Optional.of(configuration));
        when(baseRepository.findTopByOrderByVersionDesc()).thenReturn(Optional.of(base));

        patch = JsonMergePatch.fromJson(base.getDataJson());
        jsonMerged = patch.apply(configuration.getDataJson());

        assertEquals(configurationDelegate.getLastVersion(), jsonMerged);
        assertEquals(configurationDelegate.getLastVersion(), base.getDataJson());
        assertEquals(configurationDelegate.getLastVersion(), configuration.getDataJson());

    }

    @Test
    public void newConfigurationRewriteBase() throws IOException, JsonPatchException {

        baseJsonNode = mapper.readTree("{\"config1\":[\"config1\"]}");
        Base base = new Base(baseVersion, baseJsonNode);

        configurationJsonNode = mapper.readTree("{\"config1\":\"config2\"}");
        Configuration configuration = new Configuration(configurationPlatform, configurationJsonNode, configurationVersion);

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(configurationPlatform)).thenReturn(Optional.of(configuration));
        when(baseRepository.findTopByOrderByVersionDesc()).thenReturn(Optional.of(base));

        patch = JsonMergePatch.fromJson(configuration.getDataJson());
        jsonMerged = patch.apply(base.getDataJson());

        assertEquals(configurationDelegate.getLastVersion(), jsonMerged);
        assertNotEquals(configurationDelegate.getLastVersion(), baseJsonNode);
        assertEquals(configurationDelegate.getLastVersion(), configurationJsonNode);
    }

    @Test
    public void newConfigurationAddScopeToBase() throws IOException, JsonPatchException {

        baseJsonNode = mapper.readTree("{" +
                "            \"config1\": \"config1\"," +
                "            \"config3\": {" +
                "                \"config31\": \"config3.1\"," +
                "                \"config32\": \"config3.2\"" +
                "            }" +
                "        }");
        Base base = new Base(baseVersion, baseJsonNode);

        configurationJsonNode = mapper.readTree("{\"config2\": \"config2\"}");
        Configuration configuration = new Configuration(configurationPlatform, configurationJsonNode, configurationVersion);

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(configurationPlatform)).thenReturn(Optional.of(configuration));
        when(baseRepository.findTopByOrderByVersionDesc()).thenReturn(Optional.of(base));

        patch = JsonMergePatch.fromJson(configuration.getDataJson());
        jsonMerged = patch.apply(base.getDataJson());

        assertEquals(configurationDelegate.getLastVersion(), jsonMerged);
        assertNotEquals(configurationDelegate.getLastVersion(), baseJsonNode);
        assertNotEquals(configurationDelegate.getLastVersion(), configurationJsonNode);
    }

    @Test
    public void newConfigurationDeleteScope() throws IOException, JsonPatchException {
        baseJsonNode = mapper.readTree("{" +
                "            \"config1\": {" +
                "                \"config11\": \"config1.1\"," +
                "                \"config12\": \"config1.2\"" +
                "            }" +
                "        }");
        Base base = new Base(baseVersion, baseJsonNode);

        configurationJsonNode = mapper.readTree("{" +
                "            \"config1\": {" +
                "                \"config11\": \"config1.1\"," +
                "                \"config12\": null" +
                "            }" +
                "        }");
        Configuration configuration = new Configuration(configurationPlatform, configurationJsonNode, configurationVersion);

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(configurationPlatform)).thenReturn(Optional.of(configuration));
        when(baseRepository.findTopByOrderByVersionDesc()).thenReturn(Optional.of(base));

        patch = JsonMergePatch.fromJson(configuration.getDataJson());
        jsonMerged = patch.apply(base.getDataJson());

        assertEquals(configurationDelegate.getLastVersion(), jsonMerged);
        assertNotEquals(configurationDelegate.getLastVersion(), baseJsonNode);
        assertNotEquals(configurationDelegate.getLastVersion(), configurationJsonNode);
        assertNull(jsonMerged.get("config1").get("config12"));
        assertEquals(jsonMerged.get("config1").size(), 1);
    }

    @Test
    public void noConfigurationDefinedTest() throws IOException {

        configurationJsonNode = mapper.readTree("{\"config1\":\"config1\"}");

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(configurationPlatform)).thenReturn(Optional.empty());

        try {
            configurationDelegate.getLastVersion();
        } catch (Exception ex) {
            assertEquals(((ResponseStatusException) ex).getStatus(), NOT_FOUND);
        }
    }

    @Test
    public void noBaseDefinedTest() throws IOException {

        configurationJsonNode = mapper.readTree("{\"config1\":\"config1\"}");

        Configuration configuration = new Configuration(configurationPlatform, configurationJsonNode, configurationVersion);

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(configurationPlatform)).thenReturn(Optional.of(configuration));
        when(baseRepository.findTopByOrderByVersionDesc()).thenReturn(Optional.empty());

        try {
            configurationDelegate.getLastVersion();
        } catch (Exception ex) {
            assertEquals(((ResponseStatusException) ex).getStatus(), NOT_FOUND);
        }
    }
}
