import { Component } from "@angular/core";

@Component({
  selector: 'fs-files-list',
  templateUrl: './file.list.component.html'
})
export class FSFilesListComponent {

  display: boolean = false;

  showDialog() {
    this.display = true;
  }
}
