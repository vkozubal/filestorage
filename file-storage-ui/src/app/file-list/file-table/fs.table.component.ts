import { Component, Input, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { FSFile } from "../../common/file";

import "font-awesome/css/font-awesome.min.css";
import "primeng/resources/themes/omega/theme.css";
import "primeng/resources/primeng.min.css";
import { FileService } from "../../common/file.service";

const template = require('./fs.table.component.html');

@Component({
  selector: 'fs-files-table',
  template: template
})
export class TableComponent implements OnInit {
  @Input('files') files: Array<FSFile>;
  private maxDate: Date;

  private minDate: Date;
  private dateFrom: Date;
  private dateTo: Date;
  private contentTypes: Array<any>;
  onRowDblclick(event) {
    this.router.navigate([ '/files', event.data.fileId ]);
  }

  ngOnInit(): void {
    let ContentTypes: Array<string> = this.files.map(value => value.contentType);
    let uniqueContentTypes: Array<string> = Array.from(new Set<string>(ContentTypes));
    this.contentTypes = uniqueContentTypes
      .map(value => {
        const [ first, second ] = value.split('/');
        return {
          label: second,
          value: value
        }
      });

    const sorted = this.files
      .map(value => value.uploadDate)
      .sort((a, b) => {
        return a - b
      });

    this.minDate = new Date(sorted[ 0 ]);
    this.maxDate = new Date(sorted[ sorted.length - 1 ]);
  }


  public constructor(private router: Router,
                     private fService: FileService) {
  }

  resetDates(dt) {
    this.dateTo = null;
    this.dateFrom = null;
    dt.filter([], 'uploadDate', 'in');
  }

  filterByDates(dt, value, range) {
    const that = this;

    let dateFrom = (that.dateFrom || that.minDate).getTime();
    let dateTo = (that.dateTo || that.maxDate ).getTime();

    if ( dateFrom > dateTo ) {
      dateTo = dateFrom;
      that.dateTo = new Date(dateTo);
    }

    let allowedDates = this.files.filter(file => {
      let creationDate = new Date(file.uploadDate).getTime();
      return creationDate >= dateFrom
        && creationDate < dateTo + 86400000;
    }).map(value => value.uploadDate);

    allowedDates = TableComponent.handleNoRecordsFound(allowedDates);
    dt.filter(allowedDates, 'uploadDate', 'in');
    return value;
  }

  remove(file) {
    const that = this;
    this.fService.removeFile(file).then(value => {
      let index = this.files.indexOf(file);
      if ( index > -1 ) {
        that.files.splice(index, 1)
      }
    })
  }

  private static handleNoRecordsFound(allowedDates: number[]) {
    if ( allowedDates.length == 0 ) {
      // do not leave the array empty
      // if empty this filter parameter is ignored
      allowedDates.push(0);
    }
    return allowedDates;
  }
}
