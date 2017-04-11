import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { File } from "./file";

import "rxjs/add/operator/toPromise";

@Injectable()
export class FileService {
  private static filesUrl = "/api/files"; // Url to web api

  constructor(private http: Http) {
  }

  getFiles(): Promise<Array<File>> {
    return this.http.get(FileService.filesUrl)
      .toPromise()
      .then(response => {
        return response.json() as File[];
      }).catch(FileService.handleError)
  }

  getFile(id: string): Promise<File> {
    return this.http.get(`${FileService.filesUrl}/${id}/attributes`)
      .toPromise()
      .then(response => {
        return response.json() as File
      }).catch(FileService.handleError);
  }

  updateFile(file: File): Promise<File> {
    return this.http.put(`${FileService.filesUrl}/${file.id}/attributes`, file)
      .toPromise()
      .then((response) => {
        return response.json() as File
      }).catch(FileService.handleError)
  }

  private static handleError(error: any): Promise<any> {
    console.error('An error occurred', error);
    return Promise.reject(error.message || error);
  }
}
