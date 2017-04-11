import { Component } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { File, FileService} from "../common";
import { FileComment } from "../common/file";

@Component({
  selector: 'fs-file-details',
  templateUrl: './file.details.component.html'
})
export class FileDetailsComponent {
  file: File;
  comment: FileComment = new FileComment();

  constructor(private route: ActivatedRoute, private service: FileService ) {
    this.file = this.route.snapshot.data[ 'file' ];
  }

  createMessage(event) {
    console.log('create: ', event, this.file);
    this.service.updateFile(this.file);
  }

  deleteMessage(event) {
    console.log('delete: ', event, this.file);
    this.service.updateFile(this.file);
  }

  updateMessage(event) {
    console.log('update: ',event , this.file);
    this.service.updateFile(this.file);
  }

  resetComment(){
    console.log('reset value: ');
    this.comment.text = "";
  }
}
