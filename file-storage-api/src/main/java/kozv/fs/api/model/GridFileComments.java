package kozv.fs.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;

/**
 * Wrapper for file comments.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class GridFileComments extends ResourceSupport {
    private Collection<FileComment> comments;
}