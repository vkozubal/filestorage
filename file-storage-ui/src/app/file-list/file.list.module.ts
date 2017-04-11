import { NgModule } from "@angular/core";

import { FSFileUploadModule } from "./file-upload";
import { FSFileTableModule } from "./file-table";
import { CommonModule } from "../common";

import { ButtonModule, DialogModule } from "primeng/primeng";
import { FSFilesListComponent } from "./file.list.component";

@NgModule({
  declarations: [ FSFilesListComponent ],
  imports: [
    FSFileUploadModule,
    FSFileTableModule,
    CommonModule,
    DialogModule,
    ButtonModule
  ],
  exports: [ FSFilesListComponent ]
})
export class FSFilesListModule {
}
