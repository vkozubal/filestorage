import { Component } from "@angular/core";
import { Message } from "primeng/primeng";

@Component({
  selector: 'fs-file-upload',
  templateUrl: './fs.file.upload.html'
})
export class SimpleDemoComponent {

  private uploadedFiles: any[] = [];
  private msgs: Message[];

  public constructor() {
    console.log('SimpleDemoComponent');
  }

  onUpload(event) {
    console.log('uploaded');
    for ( let file of event.files ) {
      this.uploadedFiles.push(file);
    }

    this.msgs = [];
    this.msgs.push({ severity: 'info', summary: 'File Uploaded', detail: '' });
  }
}
