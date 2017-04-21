import { NgModule } from "@angular/core";

import { UploadFileComponent } from "./fs.file.upload.component";
import { CommonModule } from "@angular/common";

import { FileUploadModule } from "ng2-file-upload";

@NgModule({
  declarations: [ UploadFileComponent ],
  imports: [ CommonModule, FileUploadModule ],
  exports: [ UploadFileComponent ]
})
export class FSFileUploadModule {
}
