package kozv.fs.api.model;

import lombok.Data;
import org.springframework.hateoas.Identifiable;

/**
 * Contains all file attributes.
 */
@Data
public class FileAttributes implements Identifiable<String> {
    private String id;
    private String fileName;
    private String contentType;
    private GridFileMetadata metadata;
}