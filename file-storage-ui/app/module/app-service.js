export default class AppSvc {

  constructor($http) {

    this.$http = $http;
  }

  getData() {
    return this
      .$http.get('api/appName')
      .then(response => response.data);
  }
}