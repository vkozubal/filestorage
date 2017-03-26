import angular from 'angular';

import AppService from './app-service';
import app from './app';

export default angular.module('module', [])
  .directive('app', app)
  .service('AppSvc', AppService)
  .name;
