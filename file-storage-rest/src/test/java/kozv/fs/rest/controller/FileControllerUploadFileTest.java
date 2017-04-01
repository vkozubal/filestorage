package kozv.fs.rest.controller;

import kozv.fs.api.service.exception.PersistentFileNotFoundException;
import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.service.IFileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerUploadFileTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IFileStorageService fileStorageService;

    @Test
    public void shouldSaveFile() {
        ClassPathResource resource = new ClassPathResource("test-upload.txt", getClass());

        MultiValueMap<String, Object> map = createMultipartRequest(resource);
        //        map.add("metadata", new GridFileMetadata());
        ResponseEntity<DataFile> fileAttributesEntity = restTemplate.postForEntity("/api/files", map, DataFile.class);
        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
        then(fileStorageService).should().save(any(DataFile.class));
    }

    @Test
    public void shouldSuccessfullyFindFileAttributes() {
        when(fileStorageService.findOne(TestConstants.FILE_ID)).thenReturn(TestConstants.DATA_FILE);
        ResponseEntity<FileAttributes> fileAttributesEntity = restTemplate.getForEntity(TestConstants.GET_FILE_ATTRS_URL, FileAttributes.class, TestConstants.FILE_ID);

        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        TestConstants.assertFileAttributes(fileAttributesEntity.getBody());
    }

    @Test
    public void shouldServeFile() {
        when(fileStorageService.findOne(TestConstants.FILE_ID)).thenReturn(TestConstants.DATA_FILE);
        ResponseEntity<byte[]> fileAttributesEntity = restTemplate.getForEntity(TestConstants.FILE_URL, byte[].class, TestConstants.FILE_ID);

        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(fileAttributesEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo(TestConstants.ATTACHMENT_HEADER);
        assertThat(fileAttributesEntity.getBody()).isNotEmpty();
    }

    @Test
    public void shouldfindAllFilesAllAttributes() {
        when(fileStorageService.findAll()).thenReturn(Collections.singletonList(TestConstants.DATA_FILE.getFileAttrs()));

        ResponseEntity<Collection<FileAttributes>> fileAttributesEntity = restTemplate.exchange(TestConstants.ALL_FILES_URL, HttpMethod.GET,
                null, new ParameterizedTypeReference<Collection<FileAttributes>>() {
                });
        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Collection<FileAttributes> fileAttributesList = fileAttributesEntity.getBody();
        assertThat(fileAttributesList.size()).isEqualTo(1);
        TestConstants.assertFileAttributes(fileAttributesList.iterator().next());
    }

    @Test
    public void shouldHandleFileNotFound() {
        when(fileStorageService.findOne(anyString())).thenThrow(new PersistentFileNotFoundException());
        ResponseEntity<FileAttributes> fileAttributesEntity = restTemplate.getForEntity(TestConstants.GET_FILE_ATTRS_URL, FileAttributes.class, TestConstants.FILE_ID);
        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    private static MultiValueMap<String, Object> createMultipartRequest(ClassPathResource resource) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);
        return map;
    }
}