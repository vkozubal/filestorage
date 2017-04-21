import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { FSFile, FileComment } from "./file";

import "rxjs/add/operator/toPromise";

@Injectable()
export class CommentsService {
  constructor(private http: Http) {
  }

  getComments(file: FSFile): Promise<Array<FileComment>> {
    let url = file._links[ 'comments' ].href;
    return this.http.get(url)
      .toPromise()
      .then(response => {
        return (response.json()._embedded || {}).fileCommentList as FileComment[];
      }).catch(CommentsService.handleError)
  }

  deleteComment(comment: FileComment) {
    return this.http.delete(comment._links[ 'self' ].href)
      .toPromise();
  }

  updateComment(comment: FileComment): Promise<FileComment> {
    return this.http.put(comment._links[ 'self' ].href, comment)
      .toPromise()
      .then(CommentsService.asComment);
  }

  createComment(file: FSFile, comment: FileComment): Promise<FileComment> {
    return this.http.post(file._links[ 'comments' ].href, comment)
      .toPromise()
      .then(CommentsService.asComment)
  }

  private static asComment(response) {
    return response.json() as FileComment
  }

  private static handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}
