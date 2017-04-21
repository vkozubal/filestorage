import { NgModule } from "@angular/core";

import { DialogModule } from "primeng/primeng";

import { FSFileUploadModule } from "./file-upload";
import { FSFileTableModule } from "./file-table";
import { FsCommonModule } from "../common";
import { FSFilesListComponent } from "./file.list.component";

@NgModule({
  declarations: [ FSFilesListComponent ],
  imports: [
    FSFileUploadModule,
    FSFileTableModule,
    FsCommonModule,
    DialogModule
  ],
  exports: [ FSFilesListComponent ]
})
export class FSFilesListModule {
}
