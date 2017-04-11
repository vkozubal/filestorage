import { Component } from "@angular/core";
import { ActivatedRoute , Router} from "@angular/router";
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
  private extentions: Array<any>;

  onRowSelect(row){
    console.log('Row selected: ', row)
  }
  onRowDblclick(event){
    this.router.navigate(['/files', event.data.id]);
  }

  public constructor(private route: ActivatedRoute, private router: Router) {
    this.files = this.route.snapshot.data[ 'files' ];

    // this.extentions = [ {
    //   label: 'xls',
    //   value: "application/xls"
    // }, {
    //   label: 'pdf',
    //   value: 'application/pdf'
    // } ];

    this.extentions = [];
    this.extentions.push({ label: 'White', value: 'White' });
    this.extentions.push({ label: 'Green', value: 'Green' });
    this.extentions.push({ label: 'Silver', value: 'Silver' });
    this.extentions.push({ label: 'Maroon', value: 'Maroon' });
    this.extentions.push({ label: 'Red', value: 'Red' });
    this.extentions.push({ label: 'Orange', value: 'Orange' });
    this.extentions.push({ label: 'Blue', value: 'Blue' });
  }
}
