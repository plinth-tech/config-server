package tech.plinth.config.delegate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import tech.plinth.config.database.model.Base;
import tech.plinth.config.database.repository.BaseRepository;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RunWith(SpringRunner.class)
public class BaseDelegateTest {

    @Mock
    private BaseRepository baseRepository;

    @InjectMocks
    private BaseDelegate baseDelegate;

    @Test
    public void getNullBaseTest() {
        when(baseRepository.findTopByOrderByVersionDesc()).thenReturn(Optional.empty());

        try {
            baseDelegate.getBase();
            fail("Exception not thrown");
        } catch (ResponseStatusException ex) {
            assertEquals(ex.getStatus(), NOT_FOUND);
        }
    }


    @Test
    public void getBaseTest() throws IOException {
        Long version = 1L;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\"config1\":\"new config1.0\"}");

        Base base = new Base(version, jsonNode);

        when(baseRepository.findTopByOrderByVersionDesc()).thenReturn(Optional.of(base));
        assertEquals(base.getDataJson(), baseDelegate.getBase());
    }

}
