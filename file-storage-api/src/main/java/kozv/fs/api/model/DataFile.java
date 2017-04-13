package kozv.fs.api.model;

import lombok.Data;

import java.io.InputStream;

@Data
public class DataFile {
    /**
     * Contains an InputStream from which data can be read.
     */
    private InputStream dataStream;

    /**
     * File attributes
     */
    private FileAttributes fileAttrs;
}