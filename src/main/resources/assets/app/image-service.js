/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("ImageService", function ($q) {
		
		// Methods
		return {
			load: function (url) {
				var deferred, image;
				deferred = $q.defer();
				image = new Image();
				
				image.onload = function () {
					deferred.resolve(this);
				};
				image.error = function () {
					deferred.reject(false);
				};

				image.src = url;
				return deferred.promise;
			}
		};
		
	});

}());