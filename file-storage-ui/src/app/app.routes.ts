import { Routes } from "@angular/router";

import { FSFilesListComponent } from "./file-list";
import { FileDetailsComponent } from "./file-details";
import { FileResolver, FilesResolver } from "./common";

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
      file: FileResolver
    }
  }
  // @formatter:off
  /*{
    path: 'github', component: RepoBrowserComponent,
    children: [
      { path: '', component: RepoListComponent },
      {
        path: ':org', component: RepoListComponent,
        children: [
          { path: '', component: RepoDetailComponent },
          { path: ':repo', component: RepoDetailComponent }
        ]
      } ]
  },*/
  // @formatter:off
];
