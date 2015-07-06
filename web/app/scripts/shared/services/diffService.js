/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.service("DiffService", function() {

		this.getChanges = function(original, changed) {
			var changes = {},
				prop;
			for (prop in changed) {
				if (changed.hasOwnProperty(prop) && original.hasOwnProperty(prop)) {
					if (!angular.equals(original[prop], changed[prop])) {
						changes[prop] = changed[prop];
					}
				}
			}
			return changes;
		};
	});

}());
