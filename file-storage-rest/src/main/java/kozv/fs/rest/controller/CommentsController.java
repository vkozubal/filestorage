package kozv.fs.rest.controller;

import kozv.fs.api.model.FileComment;
import kozv.fs.api.rest.ICommentsClient;
import kozv.fs.service.api.ICommentsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Controller
@AllArgsConstructor(onConstructor = @__({@Autowired}))
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class CommentsController implements ICommentsClient {

    private final ICommentsService commentsService;

    @Override
    public Resources<FileComment> getComments(@PathVariable String fileId) {
        final Collection<FileComment> comments = commentsService.getComments(fileId);
        comments.forEach(comment -> addSelfLink(fileId, comment));
        return new Resources<>(comments);
    }

    @Override
    public FileComment createComment(@PathVariable String fileId, @RequestBody FileComment comment) {
        final FileComment entity = commentsService.createComment(fileId, comment);
        addSelfLink(fileId, entity);
        return entity;
    }

    @Override
    public void deleteComment(@PathVariable String fileId, @PathVariable String commentId) {
        commentsService.deleteComment(fileId, commentId);
    }

    @Override
    public FileComment getComment(@PathVariable String fileId, @PathVariable String commentId) {
        return commentsService.getComment(fileId, commentId);
    }

    @Override
    public FileComment updateComment(@PathVariable String commentId, @PathVariable String fileId,
                                     @RequestBody FileComment comment) {
        comment.setCommentId(commentId);
        return commentsService.updateComment(fileId, comment);
    }

    private void addSelfLink(String fileId, FileComment entity) {
        entity.add(linkTo(methodOn(getClass()).getComment(fileId, entity.getCommentId())).withSelfRel());
    }
}