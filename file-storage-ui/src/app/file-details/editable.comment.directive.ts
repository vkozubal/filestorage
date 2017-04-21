import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from "@angular/core";
import { FileComment } from "../common";
const template = require("./editable.comment.directive.html");

@Component({
  selector: 'fs-editable-comment',
  template: template
})
export class EditableCommentDirective implements OnInit {
  @Input('value') private comment: FileComment;

  // tells if the comment field is place for new comment or for existing one
  @Input('persisted') private persisted: boolean;

  @Output('onUpdate') updateEmitter = new EventEmitter();
  @Output('onDelete') deleteEmitter = new EventEmitter();
  @ViewChild('editableInput') input: any;

  private lastSavedText: string;
  private editMode: boolean;

  ngOnInit(): void {
    this.lastSavedText = this.comment.text;
    this.setEditMode(!this.persisted);
  }

  update() {
    this.updateEmitter.emit(this.comment);
    this.resetLastSavedText();
    this.setDisabledMode();
  }

  private resetLastSavedText() {
    if ( this.persisted ) {
      // in place editing
      this.lastSavedText = this.comment.text;
    } else {
      // leave the field empty
      this.lastSavedText = null;
    }
  }

  private setDisabledMode() {
    if ( this.persisted ) {
      this.editMode = false;
    }
  }

  reset() {
    this.comment.text = this.lastSavedText;
    this.setDisabledMode();
  }

  hasChanges() {
    // resolve case with null and undefined
    return (this.comment.text || false) != (this.lastSavedText || false);
  }

  hasText() {
    return this.comment.text ? this.comment.text.length : 0;
  }

  delete() {
    this.deleteEmitter.emit();
  }

  setEditMode(mode) {
    this.input.nativeElement.contentEditable = mode;
    this.editMode = mode;
  }

  enableEditing() {
    this.setEditMode(true);
  }
}
