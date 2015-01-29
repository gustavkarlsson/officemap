/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("CreatePersonController", function ($scope, $location, PersonService) {
		$scope.person = {};
		
		$scope.save = function () {
			var promise = PersonService.create($scope.person);
			promise.then(function (ref) {
				$location.url("/admin/persons/" + ref);
				//TODO toast success
			}, function (reason) {
				alert('Failed: ' + reason);
				//TODO toast failed
			});
		};
	});

}());