import { Directive, ElementRef, EventEmitter, Input, OnChanges, Output } from "@angular/core";
import { isPropertyUpdated } from "@angular/forms/src/directives/shared";

@Directive({
  selector: '[contenteditableModel]',
  host: {
    '(keyup)': 'onBlur()'
  }
})
export class ContenteditableModel implements OnChanges {
  @Input('contenteditableModel') model: any;
  @Output('contenteditableModelChange') update = new EventEmitter();

  private lastViewModel: any;

  constructor(private elRef: ElementRef) {
  }

  // changes from model to view
  ngOnChanges(changes) {
    if ( isPropertyUpdated(changes, this.lastViewModel) ) {
      this.lastViewModel = this.model;
      this.refreshView();
    }
  }

  // changes from view to model
  onBlur() {
    const value = this.elRef.nativeElement.innerText;
    this.lastViewModel = value;
    this.update.emit(value);
  }

  private refreshView() {
    this.elRef.nativeElement.innerText = this.model;
  }
}
