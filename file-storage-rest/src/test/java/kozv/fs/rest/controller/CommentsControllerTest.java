package kozv.fs.rest.controller;

import kozv.fs.api.model.FileComment;
import kozv.fs.service.api.ICommentsService;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static kozv.fs.rest.controller.FileTestConstants.ALL_FILES_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommentsController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentsControllerTest {
    private static final String FILE_ID = "file-id";
    private static final String COMMENT_ID = "comment-id";
    private static final String COMMENTS_URL = ALL_FILES_URL + "/" + FILE_ID + "/comments";
    private static final String COMMENT_URL = COMMENTS_URL + "/{comment-id}";

    @Autowired
    private TestRestTemplate restTemplate;
    @MockBean
    private ICommentsService commentsService;

    @Test
    public void shouldCreateFileComments() {
        when(commentsService.createComment(anyString(), any(FileComment.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[1]);

        ResponseEntity<FileComment> commentEntity =
                restTemplate.postForEntity(COMMENTS_URL, getFileComment(), FileComment.class);
        assertThat(commentEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final FileComment comment = commentEntity.getBody();
        assertSelfLink(comment);
    }

    @Test
    public void shouldDeleteComment() {
        final ResponseEntity<Void> responseEntity = restTemplate.exchange(COMMENT_URL, HttpMethod.DELETE, null, Void.class, COMMENT_ID);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(commentsService).deleteComment(FILE_ID, COMMENT_ID);
    }

    @Test
    public void shouldGetComment() {
        reset(commentsService);
        when(commentsService.getComment(FILE_ID, COMMENT_ID)).thenReturn(getFileComment());
        final ResponseEntity<FileComment> entity = restTemplate.getForEntity(COMMENT_URL, FileComment.class, COMMENT_ID);

        verify(commentsService).getComment(FILE_ID, COMMENT_ID);
        assertThat(entity.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldUpdateComment() {
        when(commentsService.updateComment(anyString(), any(FileComment.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[1]);

        final FileComment comment = getFileComment();
        final HttpEntity<FileComment> entity = new HttpEntity<>(comment);

        final ResponseEntity<FileComment> updateEntity = restTemplate.exchange(COMMENT_URL, HttpMethod.PUT, entity, FileComment.class, comment.getCommentId());
        assertSelfLink(updateEntity.getBody());
        assertThat(updateEntity.getStatusCode()).isEqualTo(OK);
        verify(commentsService).updateComment(FILE_ID, comment);

    }

    @Test
    public void shouldGetAllComments() {
        when(commentsService.getComments(FILE_ID)).thenReturn(new ArrayList<>(Collections.singletonList(getFileComment())));
        final ResponseEntity<Resources<FileComment>> commentsEntity = restTemplate
                .exchange(COMMENTS_URL, HttpMethod.GET, null, new ParameterizedTypeReference<Resources<FileComment>>() {
                });

        verify(commentsService).getComments(FILE_ID);
        final Collection<FileComment> comments = commentsEntity.getBody().getContent();
        assertThat(comments.size()).isEqualTo(1);
        assertSelfLink(comments.iterator().next());
        assertThat(commentsEntity.getStatusCode()).isEqualTo(OK);
    }

    private void assertSelfLink(FileComment comment) {
        assertThat(comment.getLink(Link.REL_SELF).getHref()).endsWith(COMMENTS_URL + "/1");
    }

    private FileComment getFileComment() {
        final FileComment request = new FileComment();
        request.setCommentId("1");
        request.setText("Comment 1");
        return request;
    }
}