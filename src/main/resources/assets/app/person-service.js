/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("PersonService", function ($http, $q) {
		return {
			get: function (ref) {
				return $http.get("/api/persons/" + ref).then(
					function (response) {
						if (typeof response.data === "object") {
							return response.data;
						} else {
							// invalid response
							return $q.reject(response.data);
						}

					},
					function (response) {
						// something went wrong
						return $q.reject(response.data);
					}
				);
			},
			getAll: function () {
				var deferred = $q.defer();
				$http.get("/api/persons/")
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
				$http.post("/api/persons/", person)
					.success(function (data, status) {
						deferred.resolve(data);
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
			delete: function (ref) {
				var deferred = $q.defer();
				$http.delete("/api/persons/" + ref)
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