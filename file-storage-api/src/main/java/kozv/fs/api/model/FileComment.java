package kozv.fs.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.util.Date;

/**
 * Represents the text message.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class FileComment extends ResourceSupport {
    private String commentId;
    private String text;
    private Date creationDate;
    private Date updateDate;
}