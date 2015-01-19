/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("ImageService", function ($q) {
		
		// Methods
		return {
			get: function (sha1) {
				var deferred, image;
				deferred = $q.defer();
				image = new Image();
				
				image.onload = function () {
					deferred.resolve(this);
				};
				image.error = function () {
					deferred.reject(false);
				};

				image.src = "/api/files/" + sha1;
				return deferred.promise;
			}
		};
		
	});

}());