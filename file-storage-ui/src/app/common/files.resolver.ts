import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve } from "@angular/router";
import { File } from "./file";
import { FileService } from "./file.service";
import { Observable } from "rxjs/Rx";

@Injectable()
export class FilesResolver implements Resolve<File> {
  constructor( private backend: FileService ) {
  }

  resolve( route: ActivatedRouteSnapshot ): Observable<any> | Promise<any> | any {
    return this.backend.getFiles();
  }
}

@Injectable()
export class FileResolver implements Resolve<File> {
  constructor( private backend: FileService ) {
  }

  resolve( route: ActivatedRouteSnapshot ): Observable<any> | Promise<any> | any {
    return this.backend.getFile(route.params[ 'id' ]);
  }
}
