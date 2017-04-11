export class File {
  id: string;
  fileName: string;
  contentType: string;
  metadata: FileMetadata
}

export class FileMetadata {
  uploadDate: Date;
  comments: Array<FileComment>
}

export class FileComment {
  id: string;
  text: string;
  creationDate: Date;
  updateDate: Date;

  public constructor(){
    this.text = null;
  }
}
