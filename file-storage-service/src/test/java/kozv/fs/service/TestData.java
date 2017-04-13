package kozv.fs.service;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.model.FileComment;

import java.io.InputStream;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

class TestData {
    static final String TEMP_FILE_NAME = "scratchFile-1.txt";

    static DataFile getDataFile(String fileName, InputStream data) {
        final DataFile file = new DataFile();
        file.setDataStream(data);
        file.setFileAttrs(new FileAttributes());
        file.getFileAttrs().setContentType("text/plain");
        file.getFileAttrs().setFileName(fileName);
        return file;
    }

    static DataFile getDataFile() {
        return getDataFile(TEMP_FILE_NAME, getFileStream());
    }

    static void assertFileAttributes(Date beforeCreationDate, String fileName, FileAttributes fileAttrs) {
        assertThat(fileAttrs.getUploadDate()).isBetween(beforeCreationDate, new Date());
        assertThat(fileAttrs.getFileName().equals(fileName));
        assertThat(fileAttrs.getContentType()).isEqualTo("text/plain");
        assertThat(fileAttrs.getFileId()).isNotBlank();
    }

    static FileComment getComment() {
        final FileComment comment = new FileComment();
        comment.setText("Comment 1");
        return comment;
    }

    static void assertCommentContent(FileComment comment, Date beforeCreationDate){
        assertThat(comment.getText()).isEqualTo(getComment().getText());
        assertThat(comment.getCreationDate()).isBetween(beforeCreationDate, new Date());
    }

    private static InputStream getFileStream(String fileName) {
        return TestData.class.getResourceAsStream(fileName);
    }

    static InputStream getFileStream() {
        return getFileStream(TEMP_FILE_NAME);
    }
}