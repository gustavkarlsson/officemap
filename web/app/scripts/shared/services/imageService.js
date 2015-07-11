/* global angular */
/* global FormData */

(function() {
	"use strict";
	var app = angular.module("main");

	app.service("ImageService", function($q, $http) {

		this.get = function(sha1, size) {
			var deferred, image, sizeQuery;
			deferred = $q.defer();
			image = new Image();

			image.onload = function() {
				deferred.resolve(this);
			};
			image.error = function() {
				deferred.reject(false);
			};
      if (size) {
        sizeQuery = "?size=" + size;
      }
			image.src = "/api/files/" + sha1 + sizeQuery;
			return deferred.promise;
		};

		this.upload = function(file) {
			var deferred = $q.defer(),
				formData = new FormData();
			formData.append("file", file);
			$http.post("/api/files/", formData, {
				transformRequest: angular.identity,
				headers: {
					"Content-Type": undefined
				}
			}).success(function(sha1) {
				deferred.resolve(sha1);
			}).error(function(data) {
				deferred.reject(data);
			});

			return deferred.promise;
		};

	});

}());
