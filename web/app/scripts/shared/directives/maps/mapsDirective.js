/* global alert */
/* global angular */
"use strict";

(function() {
	var app = angular.module("main");

	app.controller("MapsController", function($scope, MapService) {

		// Members
		$scope.maps = [];

		// Init
		MapService.getAll().then(
			function(maps) {
				$scope.maps = maps;
			},
			function(reason) {
				alert("Failed: " + reason);
			}
		);
	});

	app.directive("maps", function() {
		return {
			restrict: "E",
			scope: {
				maps: "=",
				activeMapRef: "="
			},
			templateUrl: "/scripts/shared/directives/maps/mapsView.html"
		};
	});

}());
