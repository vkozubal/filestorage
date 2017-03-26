import angular from 'angular';
import mainApp from './module';

import './index.less';

const appName = 'file-storage';
const module = angular.module(appName, [
  mainApp
]);

angular.element(document)
  .ready(() => angular.bootstrap(document, [appName]), {
    strictDi: true
  });

export default module.name;
