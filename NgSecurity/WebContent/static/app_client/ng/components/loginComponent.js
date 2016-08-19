var app = angular.module('applicationModule');

app.component('loginComponent', {
  controller : function($http,$window) {
	  var vm = this;

	  vm.user = {};

	  vm.login = function(user) {
	    console.log(user);
	    $http({
	      method : 'POST',
	      url : 'auth/login',
	      headers : {
	        'Content-Type' : 'application/json'
	      },
	      data : user
	    }).then(function(data) {
	    	console.log(data);
	    	$window.localStorage["todo-token"] = data.data.jwt;
	    }, function(err) {
	    	console.log(err.data.message);
	    })
	  }
	},
  controllerAs : "ctrl",
  template : function($element, $attrs) {
    return `
      <div>
        <form name="loginForm" novalidate>
          <input type="text" ng-model="ctrl.user.username" placeholder="username" /><br />
          <input type="text" ng-model="ctrl.user.password" placeholder="password" /><br />
          <input type="button" ng-click="ctrl.login(ctrl.user)" value="login" />
        </form>
      </div>
    `
  }
})