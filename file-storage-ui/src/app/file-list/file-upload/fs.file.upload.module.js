"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length,
      r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
  };
var core_1 = require("@angular/core");
var file_upload_component_1 = require("./file.upload.component.js");
var file_upload_module_1 = require("ng2-file-upload/file-upload/file-upload.module");
var common_1 = require("@angular/common");
var FSFileUploadModule = (function () {
  function FSFileUploadModule() {
  }

  FSFileUploadModule = __decorate([
    core_1.NgModule({
      declarations: [file_upload_component_1.SimpleDemoComponent],
      imports: [file_upload_module_1.FileUploadModule, common_1.CommonModule],
      exports: [file_upload_component_1.SimpleDemoComponent]
    })
  ], FSFileUploadModule);
  return FSFileUploadModule;
}());
exports.FSFileUploadModule = FSFileUploadModule;
