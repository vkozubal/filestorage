package kozv.fs.rest.controller;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class FileTestConstants {
    private static final String FILE_NAME = "test-upload.txt";
    static final String ATTACHMENT_HEADER = "attachment; filename=\"" + FILE_NAME + "\"";
    static final String FILE_ID = "file-id";
    static final String ALL_FILES_URL = "/api/files";
    static final String FILE_URL = ALL_FILES_URL + "/{fileId}";
    static final String GET_FILE_ATTRS_URL = FILE_URL + "/attributes";
    static final DataFile DATA_FILE;
    private static final String TEXT_PLAIN_CONTENT_TYPE = "text/plain";

    static {
        DATA_FILE = new DataFile();
        final InputStream resourceAsStream = FileTestConstants.class.getResourceAsStream(FILE_NAME);
        DATA_FILE.setDataStream(resourceAsStream);
        DATA_FILE.setFileAttrs(new FileAttributes());
        DATA_FILE.getFileAttrs().setFileId(FILE_ID);
        DATA_FILE.getFileAttrs().setFileName(FILE_NAME);
        DATA_FILE.getFileAttrs().setContentType(TEXT_PLAIN_CONTENT_TYPE);
    }

    static MultiValueMap<String, Object> createPostRequest() {
        return createPostRequest(FILE_NAME);
    }

    static MultiValueMap<String, Object> createPostRequest(String fileName) {
        ClassPathResource resource = new ClassPathResource(fileName, FileTestConstants.class);
        return createMultipartRequest(resource);
    }

    static MultiValueMap<String, Object> createMultipartRequest(ClassPathResource resource) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);
        return map;
    }

    static void assertFileAttributes(FileAttributes fileAttrs) {
        assertThat(fileAttrs.getFileName()).isEqualTo(FILE_NAME);
        assertThat(fileAttrs.getContentType()).isEqualTo(TEXT_PLAIN_CONTENT_TYPE);
    }
}