/*global alert */
/*global L */
/*global angular */
/*jshint -W089 */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("DiffService", function () {
		return {
			getChanges: function (original, changed) {
				var changes = {}, prop;
				for (prop in changed) {
					if (changed.hasOwnProperty(prop) && original.hasOwnProperty(prop)) {
						if (!angular.equals(original[prop], changed[prop])) {
							changes[prop] = changed[prop];
						}
					}
				}
				return changes;
			}
		};
	});

}());