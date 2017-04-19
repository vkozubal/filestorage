import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { TableComponent } from "./fs.table.component";
import {
  ButtonModule,
  CalendarModule,
  DataTableModule,
  InputTextModule,
  MultiSelectModule,
  SharedModule
} from "primeng/primeng";

@NgModule({
  declarations: [ TableComponent ],
  imports: [
    DataTableModule,
    SharedModule,
    ButtonModule,
    CalendarModule,
    MultiSelectModule,
    InputTextModule,
    CommonModule,
    FormsModule
  ],
  exports: [ TableComponent ]
})
export class FSFileTableModule {
}
