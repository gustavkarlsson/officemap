/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.factory("PersonService", function ($http, $q) {
		return {
			getPerson: function (ref) {
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
			getPersons: function () {
				var deferred = $q.defer();
				$http.get("api/persons/")
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