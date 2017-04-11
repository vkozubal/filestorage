import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { TableComponent } from "./fs.table.component";
import { ButtonModule, DataTableModule, InputTextModule, MultiSelectModule, SharedModule } from "primeng/primeng";

@NgModule({
  declarations: [ TableComponent ],
  imports: [
    DataTableModule,
    SharedModule,
    ButtonModule,
    MultiSelectModule,
    InputTextModule,
    CommonModule,
    FormsModule
  ],
  exports: [ TableComponent ]
})
export class FSFileTableModule {
}
