export class FSFile {
  fileId: string;
  fileName: string;
  contentType: string;
  metadata: FileMetadata;
  uploadDate: number;
  _links: Object
}

export class FileMetadata {
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
