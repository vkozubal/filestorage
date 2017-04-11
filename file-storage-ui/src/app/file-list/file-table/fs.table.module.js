"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length,
      r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
  };
var core_1 = require("@angular/core");
var ng2_table_1 = require("ng2-table/ng2-table");
var common_1 = require("@angular/common");
var table_demo_1 = require("./../table-demo");
var pagination_1 = require("ng2-bootstrap/pagination");
var forms_1 = require("@angular/forms");
var table_component_1 = require("./../table.component.ts");
var FSFileTableModule = (function () {
  function FSFileTableModule() {
  }

  FSFileTableModule = __decorate([
    core_1.NgModule({
      declarations: [table_component_1.TableComponent, table_demo_1.TableDemoComponent],
      imports: [
        ng2_table_1.Ng2TableModule,
        pagination_1.PaginationModule.forRoot(),
        common_1.CommonModule,
        forms_1.FormsModule
      ],
      exports: [table_component_1.TableComponent, table_demo_1.TableDemoComponent]
    })
  ], FSFileTableModule);
  return FSFileTableModule;
}());
exports.FSFileTableModule = FSFileTableModule;
