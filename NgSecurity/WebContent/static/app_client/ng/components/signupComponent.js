var app = angular.module('applicationModule');

app.component('signupComponent', {
  controller : function($http,$window) {
	  var vm = this;

	  vm.user = {};
	  vm.confirmPassword = "";

	  vm.signup = function(user) {
	    console.log(user);
	    if (user.password === vm.confirmPassword) {
	      $http({
	        method : 'POST',
	        url : 'auth/signup',
	        headers : {
	          'Content-Type' : 'application/json'
	        },
	        data : user
	      }).then(function(data){
	    	  console.log(data);
	    	  $window.localStorage['todo-token'] = data.data.jwt;
	      }, function(err){
	    	  console.log(err);
	      })
	    } else {
	      console.log("passwords do not match");
	    }
	  }
	},
  controllerAs : "ctrl",
  template : function($element, $attrs) {
    return `
      <div>
        <form name="signupForm" novalidate>
          <input type="text" ng-model="ctrl.user.username" placeholder="username" /><br />
          <input type="text" ng-model="ctrl.user.password" placeholder="password" /><br />
          <input type="text" ng-model="ctrl.confirmPassword" placeholder="confirm password" /><br />
          <input type="button" ng-click="ctrl.signup(ctrl.user)" value="signup"/>
        </form>
      </div>
    `
  }
})