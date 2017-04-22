import { Routes } from "@angular/router";

import { FSFilesListComponent } from "./file-list";
import { FileDetailsComponent } from "./file-details";
import { CommentsResolver, FileResolver, FilesResolver } from "./common";

export const rootRouterConfig: Routes = [
  { path: '', redirectTo: 'files', pathMatch: 'full' },
  {
    path: 'files',
    component: FSFilesListComponent,
    resolve: {
      files: FilesResolver
    }
  },
  {
    path: 'files/:id',
    component: FileDetailsComponent,
    resolve: {
      file: FileResolver,
      comments: CommentsResolver
    }
  }
];
