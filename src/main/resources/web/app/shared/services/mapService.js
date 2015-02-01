/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("MapService", function ($http, $q) {

		// Members
		var active;

		// Methods
		return {
			get: function (ref) {
				var deferred = $q.defer();
				$http.get("/api/maps/" + ref)
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			getAll: function () {
				var deferred = $q.defer();
				$http.get("/api/maps/")
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			create: function (map) {
				var deferred = $q.defer();
				$http.post("/api/maps/", map)
					.success(function (ref) {
						deferred.resolve(ref);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			update: function (ref, changes) {
				var deferred = $q.defer();
				$http.patch("/api/maps/" + ref, changes)
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			remove: function (ref) {
				var deferred = $q.defer();
				$http["delete"]("/api/maps/" + ref)
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			getActive: function () {
				return active;
			},
			setActive: function (ref) {
				active = ref;
				return;
			}
		};

	});

}());