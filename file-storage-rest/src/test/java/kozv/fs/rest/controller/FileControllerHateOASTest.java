package kozv.fs.rest.controller;

import kozv.fs.api.service.IFileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerHateOASTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IFileStorageService fileStorageService;

    @Test
    public void shouldContainLinkForSelfDownload(){

    }
}