package kozv.fs.api.model;

import java.util.Collection;
import java.util.Date;

/**
 * Contains all metadata related to file.
 */
public class GridFileMetadata {
    private String extension;
    private Date uploadDate;
    private Collection<FileComment> comments;
}