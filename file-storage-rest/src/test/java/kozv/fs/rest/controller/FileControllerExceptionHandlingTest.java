package kozv.fs.rest.controller;

import kozv.fs.api.service.IFileStorageService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = FileController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerExceptionHandlingTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @MockBean
    private IFileStorageService fileStorageService;

    @Test
    public void shouldHandleBigFile() throws Exception {

        /*
        thrown.expect(HttpClientErrorException.class);

        ClassPathResource resource = new ClassPathResource("empty.file", getClass());
        MultiValueMap<String, Object> map = createMultipartRequest(resource);

        Throwable thrown = catchThrowable(() -> restTemplate.postForEntity(ALL_FILES_URL, map, DataFile.class));

        assertThat(thrown )
                .isInstanceOf(HttpClientErrorException.class);

        final HttpClientErrorException exception = (HttpClientErrorException) thrown;
        assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        */
    }
}