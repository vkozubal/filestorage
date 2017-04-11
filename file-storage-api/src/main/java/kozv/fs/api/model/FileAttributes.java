package kozv.fs.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Contains all file attributes.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class FileAttributes extends ResourceSupport {
    private String fileId;
    private String fileName;
    private String contentType;
    private Date uploadDate;
}