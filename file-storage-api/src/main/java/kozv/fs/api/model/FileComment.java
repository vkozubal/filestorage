package kozv.fs.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents the text message.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FileComment extends ResourceSupport implements Serializable {
    private String commentId;
    private String text;
    private Date creationDate;
    private Date updateDate;
}