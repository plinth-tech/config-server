package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import tech.plinth.config.database.model.Configuration;
import tech.plinth.config.database.repository.ConfigurationRepository;
import tech.plinth.config.interceptor.model.RequestContext;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RunWith(SpringRunner.class)
public class ConfigurationDelegateTest {


    private Long version;
    private Long nextVersion;

    private String requestId;

    private String platform;

    private JsonNode jsonNode;

    private Configuration configuration;

    @Mock
    private ConfigurationRepository configurationRepository;
    @Mock
    private RequestContext requestContext;
    @InjectMocks
    private ConfigurationDelegate configurationDelegate;

    @Before
    public void setUp() throws IOException {
        version = 40L;
        nextVersion = 41L;

        platform = "plinth-test1";

        ObjectMapper mapper = new ObjectMapper();
        jsonNode = mapper.readTree("{\"config1\":\"config1\"}");

        configuration = new Configuration(platform, jsonNode, version);

        this.requestId = UUID.randomUUID().toString();

        when(requestContext.getRequestId()).thenReturn(this.requestId);

    }

    @Test
    public void createNewVersionTest() {
        when(requestContext.getPlatformId()).thenReturn(platform);

        when(configurationRepository.save(any(Configuration.class))).thenReturn(configuration);

        assertEquals(jsonNode, configurationDelegate.createNewVersion(jsonNode));

    }

    @Test
    public void calculateNextVersionNumberTest(){

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(platform)).thenReturn(Optional.of(configuration));

        assertEquals(nextVersion, configurationDelegate.calculateNextVersionNumber(platform));

    }


    /**
     * exception when want to calculate next version number and platform null
     */
    @Test
    public void platformNullTest() {
        try {
            configurationDelegate.calculateNextVersionNumber(null);
        } catch (Exception ex) {
            assertEquals(((ResponseStatusException) ex).getStatus(), BAD_REQUEST);
        }
    }

    /**
     * exception when want to create new version and there is no json data
     */
    @Test
    public void dataJsonNullTest() {
        try {
            configurationDelegate.createNewVersion(null);
        } catch (Exception ex) {
            assertEquals(((ResponseStatusException) ex).getStatus(), BAD_REQUEST);
        }

    }

    /**
     * exception when want to create a new version with the same version number already saved
     */
    @Test
    public void createNewWithDuplicateVersion(){

        when(configurationRepository.findTopByPlatformOrderByVersionDesc(platform)).thenReturn(Optional.of(configuration));
        when(configurationRepository.findByPlatformAndVersion(platform, nextVersion)).thenReturn(configuration);

        try {
            configurationDelegate.calculateNextVersionNumber(platform);
        } catch (Exception ex) {
            assertEquals(((ResponseStatusException) ex).getStatus(), INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * exception when platform string have more then a-z A-Z - _ ' '
     */
    @Test
    public void platformRegEx(){
        String denyPlatform = "plinth%$^~";
        when(requestContext.getPlatformId()).thenReturn(denyPlatform);

        Configuration configuration;
        configuration = new Configuration(denyPlatform, jsonNode, version);

        when(configurationRepository.save(any(Configuration.class))).thenReturn(configuration);

        try {
            configurationDelegate.createNewVersion(jsonNode);
        } catch (Exception ex) {
            assertEquals(((ResponseStatusException) ex).getStatus(), BAD_REQUEST);
        }
    }

    /**
     * accept all king of characters in json
     */
    @Test
    public void jsonWithSpecialCharacters() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNodeSpecialCharacters = mapper.readTree("{\"ﬁﬂ¢∞ ﬁﬁ@@€§\":\"㋡ ☺ ☹ ☻ 〠 シ ッ ツ ヅ Ü  ⍣ ⍤ ⍥ ⍨ ὕ ὣ Ѷ Ӫ ӫ \"}");

        when(requestContext.getPlatformId()).thenReturn(platform);

        Configuration configuration;
        configuration = new Configuration(platform, jsonNodeSpecialCharacters, version);

        when(configurationRepository.save(any(Configuration.class))).thenReturn(configuration);

        assertEquals(jsonNodeSpecialCharacters, configurationDelegate.createNewVersion(jsonNodeSpecialCharacters));

    }
}