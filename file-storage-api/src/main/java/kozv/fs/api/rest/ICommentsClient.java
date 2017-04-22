package kozv.fs.api.rest;

import kozv.fs.api.model.FileComment;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Describes the restful api for {@link FileComment}
 */
@RequestMapping(value = "/api/files/{fileId}/comments", produces = "application/hal+json")
public interface ICommentsClient {
    @GetMapping
    @ResponseBody
    Resources<FileComment> getComments(@PathVariable String fileId);

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    FileComment createComment(@PathVariable String fileId, @RequestBody FileComment comment);

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable String fileId, @PathVariable String commentId);

    @ResponseBody
    @GetMapping("/{commentId}")
    FileComment getComment(@PathVariable String fileId, @PathVariable String commentId);

    @PutMapping("/{commentId}")
    @ResponseBody
    FileComment updateComment(@PathVariable String commentId, @PathVariable String fileId, @RequestBody FileComment comment);
}