import { NgModule } from "@angular/core";
import { CommentsResolver, FileResolver, FilesResolver } from "./resolvers";
import { FileService } from "./file.service";
import { CommentsService } from "./comments.service";

@NgModule({
  providers: [
    FilesResolver, FileResolver, CommentsService,
    FileService, CommentsResolver
  ]
})
export class FsCommonModule {
}
