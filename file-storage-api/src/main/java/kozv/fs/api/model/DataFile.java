package kozv.fs.api.model;

import lombok.Data;

import java.io.InputStream;

@Data
public class DataFile {
    private InputStream data;
    private FileAttributes fileAttrs;
}