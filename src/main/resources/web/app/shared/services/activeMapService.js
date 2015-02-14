/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.service("ActiveMapService", function ($http, $q) {
		var active;

        this.get = function () {
            return active;
        };

        this.set = function (ref) {
            active = ref;
        };

	});

}());