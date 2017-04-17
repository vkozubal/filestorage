package kozv.fs.service;

import kozv.fs.api.model.DataFile;
import kozv.fs.api.model.FileAttributes;
import kozv.fs.api.model.FileComment;
import kozv.fs.api.service.ICommentsService;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static kozv.fs.service.TestData.assertCommentContent;
import static kozv.fs.service.TestData.getComment;
import static org.assertj.core.api.Java6Assertions.assertThat;

@EnableAutoConfiguration
@ComponentScan
@RunWith(SpringRunner.class)
public class FileCommentsServiceTest {

    @Autowired
    private ICommentsService commentsService;
    @Autowired
    private GridFsOperations gridFsOperations;
    private String fileId;

    @Before
    public void insertFile() {
        final DataFile file = TestData.getDataFile();
        final FileAttributes fileAttrs = file.getFileAttrs();
        fileId = gridFsOperations.store(
                file.getDataStream(),
                fileAttrs.getFileName(),
                fileAttrs.getContentType()
        ).getId().toString();
    }

    @Test
    public void shouldNotFailIfMetadataIsAbsent(){
        assertThat(commentsService.getComments(fileId)).isEmpty();
    }

    @Test
    public void storedAndRetrievedCommentShouldBeTheSame() {
        final Date beforeSaveDate = new Date();
        final FileComment createdComment = createComment();
        assertCommentContent(createdComment, beforeSaveDate);

        final FileComment fetchedComment = commentsService.getComment(fileId, createdComment.getCommentId());
        assertCommentContent(fetchedComment, beforeSaveDate);
    }

    @Test
    public void shouldDeleteComment() {
        final FileComment createdComment = createComment();

        // comments exists
        assertThat(commentsService.getComments(fileId).size()).isEqualTo(1);

        // delete comment
        commentsService.deleteComment(fileId, createdComment.getCommentId());

        final Collection<FileComment> comments = commentsService.getComments(fileId);
        assertThat(comments).isEmpty();
    }

    @Test
    public void shouldUpdateFile() {
        final FileComment createdComment = createComment();
        final String udatedCommentText = "updated comment text";
        createdComment.setText(udatedCommentText);
        Date beforeUpdateDate = new Date();
        final FileComment updatedComment = commentsService.updateComment(fileId, createdComment);

        assertThat(updatedComment.getText()).isEqualTo(udatedCommentText);

        final FileComment retrievedComment = commentsService.getComment(fileId,
                updatedComment.getCommentId());

        assertThat(retrievedComment.getText()).isEqualTo(udatedCommentText);
        assertThat(retrievedComment.getUpdateDate()).isBetween(beforeUpdateDate, new Date());
    }

    @Test
    public void shouldRetrieveAllComments(){
        // create two comments
        final String firstCommentId = createComment().getCommentId();
        final String secondCommentId = createComment().getCommentId();
        final Set<String> expectedId = Sets.newHashSet(Arrays.asList(firstCommentId, secondCommentId));

        final Collection<FileComment> comments = commentsService.getComments(fileId);
        assertThat(comments.size()).isEqualTo(2);
        final Set<String> actualId = comments.stream().map(FileComment::getCommentId).collect(Collectors.toSet());
        assertThat(actualId).isEqualTo(expectedId);
    }

    private FileComment createComment() {
        return commentsService.createComment(fileId, getComment());
    }
}