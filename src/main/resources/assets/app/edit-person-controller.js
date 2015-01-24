/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("EditPersonController", function ($scope, $location, $route, ref, person, PersonService, DiffService) {
		var originalPerson = angular.copy(person);
		
		$scope.ref = ref;
		$scope.person = person;
		
		$scope.reset = function () {
			$scope.person = angular.copy(originalPerson);
		};
		
		$scope.save = function () {
			var changes = DiffService.getChanges(originalPerson, $scope.person),
				promise;
			promise = PersonService.update($scope.ref, changes);
			promise.then(function () {
				$route.reload();
				//TODO toast success
			}, function (reason) {
				alert('Failed: ' + reason);
				//TODO toast failed
			});
		};
		
		$scope.delete = function () {
			var promise = PersonService.delete($scope.ref);
			promise.then(function () {
				$location.path("/admin");
				//TODO toast success
			}, function (reason) {
				alert('Failed: ' + reason);
				//TODO toast failed
			});
		};

		$scope.isChanged = function () {
			return !angular.equals($scope.person, originalPerson);
		};
	});

}());