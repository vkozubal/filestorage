package kozv.fs.api.model;

import lombok.Data;

import java.util.Collection;
import java.util.Date;

/**
 * Contains all metadata related to file.
 */
@Data
public class GridFileMetadata {
    private String extension;
    private Date uploadDate;
    private Collection<FileComment> comments;
}