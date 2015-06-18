/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main", ["ngRoute", "leaflet-directive", "angularFileUpload"]);

	app.config(function ($locationProvider) {
		$locationProvider.html5Mode(true);
	});

}());