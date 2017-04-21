import { Component, Input, OnInit } from "@angular/core";
import { FSFile } from "../../common";

const template = require('./fs.file.preview.component.html');

@Component({
  selector: 'fs-file-preview',
  template: template
})
export class FilePreviewComponent implements OnInit {
  @Input('file') file: FSFile;

  ngOnInit(): void {
  }
}
