package kozv.fs.rest.controller;

import kozv.fs.api.model.FileComment;
import kozv.fs.api.service.ICommentsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static kozv.fs.rest.controller.FileTestConstants.ALL_FILES_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommentsController.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommentsControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ICommentsService commentsService;

    @Test
    public void shouldReturnFileComments(){
        when(commentsService.createComment(anyString(), any(FileComment.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[1]);

        final String url = ALL_FILES_URL +  "/fileId/comments";
        final FileComment request = new FileComment();
        request.setCommentId("1");
        request.setText("Comment 1");

        ResponseEntity<FileComment> commentEntity =
                restTemplate.postForEntity(url, request, FileComment.class);
        assertThat(commentEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final FileComment comment = commentEntity.getBody();
        assertThat(comment.getLink(Link.REL_SELF).getHref()).endsWith(url + "/1");
    }
}