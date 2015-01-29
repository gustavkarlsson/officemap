/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("AdminController", function ($scope, persons, maps) {
		$scope.persons = persons;
		$scope.maps = maps;
	});

}());