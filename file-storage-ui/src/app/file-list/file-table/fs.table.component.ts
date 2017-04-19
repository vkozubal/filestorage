import { Component } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { File } from "../../common/file";

import "primeng/resources/themes/omega/theme.css";
import "primeng/resources/primeng.min.css";
import "font-awesome/css/font-awesome.min.css";
const template = require('./fs.table.component.html');

@Component({
  selector: 'fs-files-table',
  template: template
})
export class TableComponent {
  private files: Array<File> = [];
  private maxDate: Date;
  private minDate: Date;
  private dateFrom: Date;
  private dateTo: Date;
  private contentTypes: Array<any>;

  onRowSelect(row) {
  }

  onRowDblclick(event) {
    this.router.navigate([ '/files', event.data.fileId ]);
  }

  public constructor(private route: ActivatedRoute, private router: Router) {
    this.files = this.route.snapshot.data[ 'files' ];

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

  private static handleNoRecordsFound(allowedDates: number[]) {
    if ( allowedDates.length == 0 ) {
      // do not leave the array empty
      // if empty this filter parameter is ignored
      allowedDates.push(0);
    }
    return allowedDates;
  }
}
