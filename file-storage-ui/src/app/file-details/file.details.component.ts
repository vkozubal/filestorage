import { Component } from "@angular/core";
import { Http } from "@angular/http";
import { ActivatedRoute } from "@angular/router";
import { FSFile } from "../common";
import { FileComment } from "../common/file";
import { CommentsService } from "../common/comments.service";

@Component({
  selector: 'fs-file-details',
  templateUrl: './file.details.component.html'
})
export class FileDetailsComponent {
  file: FSFile;
  comments: Array<FileComment>;
  comment: FileComment = new FileComment();

  constructor(private route: ActivatedRoute,
              private commentsService: CommentsService) {

    this.file = this.route.snapshot.data[ 'file' ];
    this.comments = this.route.snapshot.data[ 'comments' ];
  }

  createMessage(event) {
    const that = this;
    return this.commentsService.createComment(this.file, event)
      .then(comment => {
          // add comment to the list
          that.comments = that.comments || [];
          that.comments.unshift(comment);
          that.comment = new FileComment();
        }
      )
  }

  deleteMessage(event) {
    const that = this;
    return this.commentsService.deleteComment(event).then(
      () => {
        // find and remove item from the list
        const index = this.findIndex(that, event);
        if ( index > -1 ) {
          that.comments.splice(index, 1);
        }
      }
    );
  }

  updateMessage(event) {
    const that = this;
    return this.commentsService.updateComment(event)
      .then(
        (updated: FileComment) => {
          // find and update item
          const index = this.findIndex(that, event);
          if ( index > -1 ) {
            that.comments.splice(index, 1, updated);
          }
        }
      );
  }

  private findIndex(that: FileDetailsComponent, event) {
    return that.comments.findIndex(value => value.commentId === event.commentId);
  }

  resetComment() {
    this.comment.text = "";
  }

  asComment(response) {
    return response.json() as FileComment
  }
}
