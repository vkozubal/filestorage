import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FilePreviewComponent } from "./fs.file.preview.component";

@NgModule({
  declarations: [ FilePreviewComponent ],
  imports: [
    CommonModule,
  ],
  exports: [ FilePreviewComponent ]
})
export class FSFilePreviewModule {
}
