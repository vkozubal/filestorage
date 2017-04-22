package kozv.fs.rest.controller;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.service.api.IFileStorageService;
import kozv.fs.service.exception.PersistentFileNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.Collections;

import static kozv.fs.rest.controller.FileTestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FileController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private IFileStorageService fileStorageService;

    @Test
    public void shouldSaveFile() {
        setupMock();

        ResponseEntity<FileAttributes> fileAttributesEntity =
                restTemplate.postForEntity(ALL_FILES_URL, createPostRequest(), FileAttributes.class);

        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);

        then(fileStorageService).should().save(any(DataFile.class));

        final FileAttributes fileAttrs = fileAttributesEntity.getBody();

        assertFileAttributes(fileAttrs);

        // assert links
        assertFileLinks(fileAttrs);
    }

    @Test
    public void shouldSuccessfullyFindFileAttributes() {
        when(fileStorageService.findOne(FileTestConstants.FILE_ID)).thenReturn(FileTestConstants.DATA_FILE);
        ResponseEntity<FileAttributes> fileAttributesEntity = restTemplate.getForEntity(FileTestConstants.GET_FILE_ATTRS_URL, FileAttributes.class, FileTestConstants.FILE_ID);

        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertFileAttributes(fileAttributesEntity.getBody());
    }

    @Test
    public void shouldServeFile() {
        when(fileStorageService.findOne(FileTestConstants.FILE_ID)).thenReturn(FileTestConstants.DATA_FILE);
        ResponseEntity<byte[]> fileAttributesEntity = restTemplate.getForEntity(FileTestConstants.FILE_URL, byte[].class, FileTestConstants.FILE_ID);

        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(fileAttributesEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION))
                .isEqualTo(FileTestConstants.ATTACHMENT_HEADER);
        assertThat(fileAttributesEntity.getBody()).isNotEmpty();
    }

    @Test
    public void shouldFindAllFilesAttributes() {
        when(fileStorageService.findAll()).thenReturn(Collections.singletonList(FileTestConstants.DATA_FILE.getFileAttrs()));

        ResponseEntity<Resources<FileAttributes>> fileAttributesEntity = restTemplate.exchange(ALL_FILES_URL, HttpMethod.GET,
                null, new ParameterizedTypeReference<Resources<FileAttributes>>() {
                });
        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        Collection<FileAttributes> fileAttributesList = fileAttributesEntity.getBody().getContent();
        assertThat(fileAttributesList.size()).isEqualTo(1);
        final FileAttributes fileAttrs = fileAttributesList.iterator().next();
        assertFileAttributes(fileAttrs);
        assertFileLinks(fileAttrs);
    }

    @Test
    public void shouldHandleFileNotFound() {
        when(fileStorageService.findOne(anyString())).thenThrow(new PersistentFileNotFoundException(""));
        ResponseEntity<FileAttributes> fileAttributesEntity = restTemplate.getForEntity(FileTestConstants.GET_FILE_ATTRS_URL, FileAttributes.class, FileTestConstants.FILE_ID);
        assertThat(fileAttributesEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldHandleBigFile() {
        setupMock();
        final MultiValueMap<String, Object> postRequest = createPostRequest("big-file.zip");

        ResponseEntity<FileAttributes> responseEntity = restTemplate.postForEntity(
                ALL_FILES_URL, postRequest, FileAttributes.class);

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    private void assertFileLinks(FileAttributes fileAttrs) {
        final Link selfLink = fileAttrs.getLink(Link.REL_SELF);
        assertThat(selfLink.getHref()).endsWith("/api/files/" + FILE_ID + "/attributes");

        final Link downloadLink = fileAttrs.getLink("download");
        assertThat(downloadLink.getHref()).endsWith("/api/files/" + FILE_ID);

        final Link commentsLink = fileAttrs.getLink("comments");
        assertThat(commentsLink.getHref()).endsWith("/api/files/" + FILE_ID + "/comments");
    }

    private void setupMock() {
        when(fileStorageService.save(any(DataFile.class)))
                .thenAnswer(invocationOnMock -> {
                    final FileAttributes fileAttrs = ((DataFile) invocationOnMock.getArguments()[0]).getFileAttrs();
                    fileAttrs.setFileId(FILE_ID);
                    return fileAttrs;
                });
    }
}