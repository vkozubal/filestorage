package kozv.fs.rest.controller;

import kozv.fs.api.model.FileComment;
import kozv.fs.api.model.GridFileComments;
import kozv.fs.api.service.ICommentsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@RequestMapping("/api/files/{fileId}/comments")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class CommentsController {

    private final ICommentsService commentsService;

    @GetMapping
    @ResponseBody
    public GridFileComments getComments(@PathVariable String fileId) {
        return commentsService.getComments(fileId);
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public FileComment createComment(@PathVariable String fileId, @RequestBody FileComment comment) {
        final FileComment entity = commentsService.createComment(fileId, comment);
        entity.add(linkTo(methodOn(getClass()).getComment(fileId, entity.getCommentId())).withSelfRel());
        return entity;
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable String fileId, @PathVariable String commentId) {
        commentsService.deleteComment(fileId, commentId);
    }

    @GetMapping("/{commentId}")
    public FileComment getComment(@PathVariable String fileId, @PathVariable String commentId) {
        return commentsService.getComment(fileId, commentId);
    }

    @PutMapping("/{commentId}")
    @ResponseBody
    public FileComment updateComment(@PathVariable String commentId, @PathVariable String fileId, FileComment comment) {
        comment.setCommentId(commentId);
        return commentsService.updateComment(fileId, comment);
    }
}