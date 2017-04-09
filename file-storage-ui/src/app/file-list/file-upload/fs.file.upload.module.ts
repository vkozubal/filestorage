import { NgModule } from "@angular/core";

import { SimpleDemoComponent } from "./fs.file.upload.component";
import { CommonModule } from "@angular/common";

import { FileUploadModule, GrowlModule, MessagesModule } from "primeng/primeng";

@NgModule({
  declarations: [ SimpleDemoComponent ],
  imports: [ FileUploadModule, CommonModule, GrowlModule, MessagesModule ],
  exports: [ SimpleDemoComponent ]
})
export class FSFileUploadModule {
}
