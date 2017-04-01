package kozv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @SuppressWarnings("unassigned")
    @LocalServerPort
    private int port;

    @Test
    public void healthCheck() throws IOException {
        // check that frontend is successfully started
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port, String.class);
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }
}