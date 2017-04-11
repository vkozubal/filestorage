import { NgModule } from "@angular/core";
import { FileResolver, FilesResolver } from "./files.resolver";
import { FileService } from "./file.service";

@NgModule({
  providers: [ FilesResolver, FileResolver, FileService ]
})
export class CommonModule {
}
