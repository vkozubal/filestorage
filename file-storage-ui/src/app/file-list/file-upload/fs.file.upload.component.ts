import { Component, EventEmitter, OnInit, Output } from "@angular/core";

import { FileUploader } from "ng2-file-upload";

const URL: string = "api/files";

@Component({
  selector: 'fs-file-upload',
  templateUrl: './fs.file.upload.html'
})
export class UploadFileComponent implements OnInit {
  @Output() uploaded = new EventEmitter();
  public uploader: FileUploader = new FileUploader({ url: URL, autoUpload: true });

  ngOnInit(): void {
  }

  fireUploaded() {
    this.uploaded.emit(null);
  }
}
