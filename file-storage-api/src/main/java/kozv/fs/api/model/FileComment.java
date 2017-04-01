package kozv.fs.api.model;

import lombok.Data;
import org.springframework.hateoas.Identifiable;

import java.util.Date;

/**
 * Represents the text message. See {@link FileAttributes#metadata#comments}
 */
@Data
public class FileComment implements Identifiable<String> {
    private String id;
    private String text;
    private Date creationDate;
    private Date updateDate;
}