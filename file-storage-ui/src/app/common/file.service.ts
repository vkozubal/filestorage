import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { FSFile } from "./file";

import "rxjs/add/operator/toPromise";

const filesUrl = "/api/files"; // Url to web api

@Injectable()
export class FileService {

  constructor(private http: Http) {
  }

  getFiles(): Promise<Array<FSFile>> {
    return this.http.get(filesUrl)
      .toPromise()
      .then(FileService.asFiles)
      .catch(FileService.handleError)
  }

  getFile(id: string): Promise<FSFile> {
    return this.http.get(`${filesUrl}/${id}`)
      .toPromise()
      .then(FileService.asFile)
      .catch(FileService.handleError);
  }

  removeFile(file: FSFile): Promise<FSFile> {
    return this.http.delete(file._links[ 'self' ].href)
      .toPromise()
      .then(FileService.asFile)
      .catch(FileService.handleError);
  }

  private static asFile(response) {
    return response.json() as FSFile;
  }

  private static asFiles(response) {
    return ((response.json()._embedded || {}).fileAttributesList || []) as FSFile[];
  }

  private static handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}
