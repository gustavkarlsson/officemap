/* global angular */
"use strict";

(function() {
	var app = angular.module("main");

	app.controller("AdminController", function($scope, persons, maps) {
		$scope.persons = persons;
		$scope.maps = maps;
	});

}());
