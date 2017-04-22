export class File {
  fileId: string;
  fileName: string;
  contentType: string;
  metadata: FileMetadata;
  _links: Object
}

export class FileMetadata {
  uploadDate: Date;
  comments: Array<FileComment>
}

export class FileComment {
  commentId: string;
  text: string;
  creationDate: Date;
  updateDate: Date;
  _links: Object;

  public constructor() {
    this.text = null;
  }
}
