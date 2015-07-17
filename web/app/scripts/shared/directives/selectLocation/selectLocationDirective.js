/* global alert */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("SelectLocationController", function() {

	});

	app.directive("selectLocation", function() {
		return {
			restrict: "E",
			templateUrl: "/scripts/shared/directives/selectLocation/selectLocationView.html"
		};
	});

}());
