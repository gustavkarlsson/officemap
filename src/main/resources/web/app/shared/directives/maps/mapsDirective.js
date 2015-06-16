/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("MapsController", function ($scope, MapService) {

        // Members
        $scope.maps = [];

		// Init
		var mapsPromise = MapService.getAll();
		mapsPromise.then(
			function (maps) {
				$scope.maps = maps;
			},
			function (reason) {
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
            templateUrl: "/app/shared/directives/maps/mapsView.html"
        };
    });

}());
