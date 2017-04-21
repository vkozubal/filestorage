package kozv.fs.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Contains all file attributes.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileAttributes extends ResourceSupport implements Serializable{
    private String fileId;
    private String fileName;
    private String contentType;
    private Date uploadDate;
}