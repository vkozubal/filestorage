package kozv.fs.service;


import com.mongodb.gridfs.GridFSDBFile;
import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.service.IFileStorageService;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kozv.fs.service.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@ComponentScan
@RunWith(SpringRunner.class)
public class FileStorageServiceTest {
    @Autowired
    private GridFsOperations gridFsOperations;
    @Autowired
    private IFileStorageService fileStorageService;

    @Test
    public void shouldFindallStoredFiles() {
        final String firstFileId = storeFile("scratchFile-1.txt").getFileId();
        final String secondFileId = storeFile("scratchFile-2.txt").getFileId();

        final Set<String> expectedIds = Sets.newHashSet(Arrays.asList(firstFileId, secondFileId));

        final List<GridFSDBFile> foundResults = gridFsOperations.find(null);
        assertThat(foundResults.size()).isEqualTo(2); // found all stored previously results

        final Set<String> foundIds = foundResults.stream()
                .map(gridFSDBFile -> gridFSDBFile.getId().toString()).collect(Collectors.toSet());

        assertThat(foundIds).isEqualTo(expectedIds);
    }

    @Test
    public void storedAndRetrievedFilesShouldBeTheSame() throws IOException {
        Date beforeSaveDate = new Date();

        final FileAttributes fileAttrs = fileStorageService.save(
                getDataFile(TEMP_FILE_NAME, getFileStream())
        );

        // check response attributes
        assertFileAttributes(beforeSaveDate, TEMP_FILE_NAME, fileAttrs);

        final String fileId = fileAttrs.getFileId();

        final DataFile storedFile = fileStorageService.findOne(fileId);

        // check content: stored file content is the same as the retrieved
        assertThat(fileAsString(storedFile.getDataStream())).isEqualTo(fileAsString(getFileStream()));
        assertThat(storedFile.getFileAttrs().getFileId()).isEqualTo(fileId);

        assertFileAttributes(beforeSaveDate, TEMP_FILE_NAME, storedFile.getFileAttrs());
    }

    private FileAttributes storeFile(String fileName) {
        final InputStream data = getClass().getResourceAsStream(fileName);
        return fileStorageService.save(getDataFile(fileName, data));
    }

    private String fileAsString(InputStream dataStream) throws IOException {
        return IOUtils.toString(dataStream, "UTF-8");
    }
}
