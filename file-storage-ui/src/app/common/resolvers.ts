import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve } from "@angular/router";
import { FSFile } from "./file";
import { FileService } from "./file.service";
import { Observable } from "rxjs/Rx";
import { CommentsService } from "./comments.service";

@Injectable()
export class FilesResolver implements Resolve<FSFile> {
  constructor(private fileService: FileService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<any> | Promise<any> | any {
    return this.fileService.getFiles();
  }
}

@Injectable()
export class FileResolver implements Resolve<FSFile> {
  constructor(private fileService: FileService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<any> | Promise<any> | any {
    return getFile(this.fileService, route);
  }
}

@Injectable()
export class CommentsResolver implements Resolve<FSFile> {
  constructor(private fileService: FileService, private commentsService: CommentsService) {
  }

  resolve(route: ActivatedRouteSnapshot): Observable<any> | Promise<any> | any {
    return getFile(this.fileService, route).then((file) => {
      return this.commentsService.getComments(file);
    })
  }
}

function getFile(backend: FileService, route: ActivatedRouteSnapshot) {
  let id = route.params[ 'id' ];
  return backend.getFile(id);
}
