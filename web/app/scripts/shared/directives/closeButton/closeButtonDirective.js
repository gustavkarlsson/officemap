/* global alert */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.directive("closeButton", function() {
		return {
			restrict: "E",
			templateUrl: "/scripts/shared/directives/closeButton/closeButtonView.html"
		};
	});

}());
