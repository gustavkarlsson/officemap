/* global alert */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("MapsController", function($scope, MapService) {

		// Members
		$scope.maps = {};

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
				activeRef: "="
			},
			templateUrl: "/scripts/shared/directives/maps/mapsView.html"
		};
	});

}());
