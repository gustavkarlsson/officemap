/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.directive("iconButton", function() {
		return {
			restrict: "E",
      scope: {
        icon: "@",
        tooltip: "@",
        uiSref: "@"
      }, 
      templateUrl: "scripts/shared/directives/iconButton/iconButtonView.html"
		};
	});

}());
