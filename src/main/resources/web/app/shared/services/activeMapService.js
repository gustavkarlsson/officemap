/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("ActiveMapService", function ($http, $q) {

		var active;

		return {
			get: function () {
				return active;
			},
			set: function (ref) {
				active = ref;
				return;
			}
		};

	});

}());