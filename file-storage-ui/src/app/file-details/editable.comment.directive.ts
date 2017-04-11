import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { FileComment } from "../common";
const template = require("./editable.comment.directive.html");

@Component({
  selector: 'fs-editable-comment',
  template: template
})
export class EditableCommentDirective implements OnInit {
  @Input('value') private comment: FileComment;
  @Output('onUpdate') updateEmitter = new EventEmitter();
  @Output('onDelete') deleteEmitter = new EventEmitter();

  private lastSavedText: string;

  ngOnInit(): void {
    this.lastSavedText = this.comment.text;
  }

  update() {
    this.updateEmitter.emit(this.comment);
    this.lastSavedText = this.comment.text
  }

  cancel() {
    this.deleteEmitter.emit();
  }
}
