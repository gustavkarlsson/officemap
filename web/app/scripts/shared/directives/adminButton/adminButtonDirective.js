/* global alert */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.directive("adminButton", function() {
		return {
			restrict: "E",
			templateUrl: "/scripts/shared/directives/adminButton/adminButtonView.html"
		};
	});

}());
