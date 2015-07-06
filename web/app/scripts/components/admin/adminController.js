/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("AdminController", function($scope, $location, persons, maps) {
		$scope.persons = persons;
		$scope.maps = maps;

    $scope.go = function(path) {
      $location.path(path);
    };

	});

}());
