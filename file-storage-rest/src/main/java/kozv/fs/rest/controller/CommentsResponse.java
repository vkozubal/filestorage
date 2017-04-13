package kozv.fs.rest.controller;

import kozv.fs.api.model.FileComment;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.util.Collection;

@AllArgsConstructor
public class CommentsResponse extends ResourceSupport {
    private Collection<FileComment> comments;
}