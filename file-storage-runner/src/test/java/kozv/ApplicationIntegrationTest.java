package kozv;

import kozv.fs.api.model.DataFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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

    @Test
    public void getFiles() {

        ClassPathResource resource = new ClassPathResource("test-upload.txt", getClass());
        MultiValueMap<String, Object> map = createMultipartRequest(resource);
        ResponseEntity<DataFile> fileAttributesEntity = restTemplate.postForEntity("/api/files", map, DataFile.class);
        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    private static MultiValueMap<String, Object> createMultipartRequest(ClassPathResource resource) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);
        return map;
    }
}