/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("PersonService", function ($http, $q) {
		return {
			get: function (ref) {
				var deferred = $q.defer();
				$http.get("/api/persons/" + ref)
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
				$http.get("/api/persons")
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			getAllByMapRef: function (ref) {
				var deferred = $q.defer();
				$http.get("/api/persons?mapRef=" + ref)
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			create: function (person) {
				var deferred = $q.defer();
				$http.post("/api/persons", person)
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
				$http.patch("/api/persons/" + ref, changes)
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
				$http["delete"]("/api/persons/" + ref)
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			}
		};
	});

}());