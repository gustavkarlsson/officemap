/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("ImageService", function ($q, $http) {

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
			},
			upload: function (file) {
				var deferred = $q.defer(),
					formData = new FormData();
				formData.append("file", file);
				$http.post("api/files/", formData, {
						transformRequest: angular.identity,
						headers: {
							"Content-Type": undefined
						}
					})
					.success(function (sha1) {
						deferred.resolve(sha1);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			}
		};

	});

}());