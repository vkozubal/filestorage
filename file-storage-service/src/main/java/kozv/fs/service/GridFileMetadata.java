package kozv.fs.service;

import kozv.fs.api.model.FileComment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

/**
 * Wraps all file Metadata
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GridFileMetadata {
    private Collection<FileComment> comments;
}
