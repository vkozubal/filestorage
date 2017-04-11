"use strict";
var about_component_1 = require('./about/about.component');
var home_component_1 = require('./home/home.component');
var repo_browser_component_1 = require('./github/repo-browser/repo-browser.component');
var repo_list_component_1 = require('./github/repo-list/repo-list.component');
var repo_detail_component_1 = require('./github/repo-detail/repo-detail.component');
var contact_component_1 = require('./contact/contact.component');
var files_resolver_1 = require("./table/common/files.resolver");
exports.rootRouterConfig = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
  {
    path: 'home', component: home_component_1.HomeComponent, resolve: {
    files: files_resolver_1.FilesResolver
  }
  },
  {path: 'about', component: about_component_1.AboutComponent},
  {
    path: 'github', component: repo_browser_component_1.RepoBrowserComponent,
    children: [
      {path: '', component: repo_list_component_1.RepoListComponent},
      {
        path: ':org', component: repo_list_component_1.RepoListComponent,
        children: [
          {path: '', component: repo_detail_component_1.RepoDetailComponent},
          {path: ':repo', component: repo_detail_component_1.RepoDetailComponent}
        ]
      }]
  },
  {path: 'contact', component: contact_component_1.ContactComponent}
];
