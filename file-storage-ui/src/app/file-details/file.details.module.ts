import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FileDetailsComponent } from "./file.details.component";
import { ContenteditableModel } from "./content.editable.model";
import { EditableCommentDirective } from "./editable.comment.directive";
import { FilePreviewComponent } from "./file-preview";
import { FSFilePreviewModule } from "./file-preview/file.preview.module";

require("./styles.less");

@NgModule({
  declarations: [ FileDetailsComponent, ContenteditableModel, EditableCommentDirective ],
  imports: [ CommonModule, FormsModule, ReactiveFormsModule, FSFilePreviewModule ],
  exports: [ FileDetailsComponent]
})
export class FSFileDetailsModule {
}
