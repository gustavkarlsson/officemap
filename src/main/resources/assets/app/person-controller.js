/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("PersonController", function ($scope, person) {
		$scope.person = person;
	});

}());