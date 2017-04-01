package kozv.fs.rest.controller;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

class TestConstants {
    private static final String FILE_NAME = "File-name.txt";
    static final String ATTACHMENT_HEADER = "attachment; filename=\"" + FILE_NAME + "\"";
    static final String FILE_ID = "file-id";
    static final String ALL_FILES_URL = "/api/files";
    static final String FILE_URL = ALL_FILES_URL + "/{fileId}";
    static final String GET_FILE_ATTRS_URL = FILE_URL + "/attributes";
    static final DataFile DATA_FILE;

    static {
        DATA_FILE = new DataFile();
        try {
            ClassPathResource resource = new ClassPathResource("test-upload.txt", TestConstants.class);
            DATA_FILE.setData(resource.getInputStream());
            DATA_FILE.setFileAttrs(new FileAttributes());
            DATA_FILE.getFileAttrs().setId(FILE_ID);
            DATA_FILE.getFileAttrs().setFileName(FILE_NAME);
        } catch (IOException e) {
            fail("Test data should exist.");
        }
    }

    static void assertFileAttributes(FileAttributes file) {
        assertEquals(FILE_NAME, file.getFileName());
        assertEquals(FILE_ID, file.getId());
    }
}