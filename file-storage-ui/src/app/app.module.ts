import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { rootRouterConfig } from "./app.routes";
import { BrowserModule } from "@angular/platform-browser";
import { HttpModule } from "@angular/http";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { AppComponent } from "./app.component";
import { FSFileDetailsModule } from "./file-details";
import { FSFilesListModule } from "./file-list";

require('./app.less');

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    FSFilesListModule,
    FSFileDetailsModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    RouterModule.forRoot(rootRouterConfig, { useHash: true })
  ],
  providers: [],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}
