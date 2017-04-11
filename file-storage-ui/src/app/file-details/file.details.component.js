"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length,
      r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
  };
var core_1 = require("@angular/core");
var forms_1 = require("@angular/forms");
var CustomValidators_1 = require("../forms/CustomValidators");
var ContactComponent = (function () {
  function ContactComponent(formBuilder, http) {
    var _this = this;
    this.formBuilder = formBuilder;
    this.http = http;
    this.files = [];
    http.get("/api/files")
      .map(function (res) { return res.json(); })
      .subscribe(function (file) { return _this.files = file; });
  }

  ContactComponent.prototype.ngOnInit = function () {
    this.contactForm = this.formBuilder.group({
      name: ['', forms_1.Validators.required],
      email: ['', [forms_1.Validators.required, CustomValidators_1.default.validateEmail]],
      content: ['', [forms_1.Validators.required, forms_1.Validators.minLength(10)]]
    });
  };
  ContactComponent.prototype.submitForm = function () {
    console.log(this.contactForm);
  };
  ContactComponent = __decorate([
    core_1.Component({
      selector: 'app-contact',
      templateUrl: './contact.component.html',
      styleUrls: ['./contact-component.css']
    })
  ], ContactComponent);
  return ContactComponent;
}());
exports.ContactComponent = ContactComponent;
