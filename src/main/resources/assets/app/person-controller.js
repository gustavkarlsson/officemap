/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("PersonController", function ($scope, person, PersonService) {
		$scope.originalPerson = JSON.parse(JSON.stringify(person));
		$scope.person = person;
		
		$scope.save = function () {
			alert("Save!");
		};
	});

}());