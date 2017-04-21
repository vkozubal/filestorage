import { Component } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { FileService } from "../common/file.service";
import { FSFile } from "../common";

require("./styles.less");

@Component({
  selector: 'fs-files-list',
  templateUrl: './file.list.component.html'
})
export class FSFilesListComponent {
  private files: Array<FSFile> = [];
  private display: boolean = false;

  constructor(private route: ActivatedRoute, private fileService: FileService) {
    this.files = this.route.snapshot.data[ 'files' ];
  }

  showDialog() {
    this.display = true;
  }

  refresh() {
    this.fileService.getFiles().then(value=> {
      this.files = value;
    });
  };
}
