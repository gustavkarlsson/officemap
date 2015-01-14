/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("MapsController", function ($scope, $http, MapService) {
		
		// Init
		var mapsPromise = MapService.getMaps();
		mapsPromise.then(
			function (maps) {
				$scope.maps = maps;
			},
			function (reason) {
				alert("Failed: " + reason);
			}
		);
		
		// Members
		$scope.maps = [];
		
		// Methods
		$scope.isActive = function (ref) {
			var active = MapService.getActive();
			return typeof active === "undefined" ? false : ref === active;
		};
		
	});

}());