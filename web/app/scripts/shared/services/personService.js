/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.service("PersonService", function($http, $q) {

		this.get = function(ref) {
			var deferred = $q.defer();
			$http.get("/api/persons/" + ref)
				.success(function(data) {
					deferred.resolve(data);
				})
				.error(function(data) {
					deferred.reject(data);
				});

			return deferred.promise;
		};

		this.getAll = function() {
			var deferred = $q.defer();
			$http.get("/api/persons")
				.success(function(data) {
					deferred.resolve(data);
				})
				.error(function(data) {
					deferred.reject(data);
				});

			return deferred.promise;
		};

		this.getAllByMapRef = function(ref) {
			var deferred = $q.defer();
			$http.get("/api/persons?mapRef=" + ref)
				.success(function(data) {
					deferred.resolve(data);
				})
				.error(function(data) {
					deferred.reject(data);
				});

			return deferred.promise;
		};

		this.create = function(person) {
			var deferred = $q.defer();
			$http.post("/api/persons", person)
				.success(function(ref) {
					deferred.resolve(ref);
				})
				.error(function(data) {
					deferred.reject(data);
				});

			return deferred.promise;
		};

		this.update = function(ref, changes) {
			var deferred = $q.defer();
			$http.patch("/api/persons/" + ref, changes)
				.success(function(data) {
					deferred.resolve(data);
				})
				.error(function(data) {
					deferred.reject(data);
				});

			return deferred.promise;
		};

		this.remove = function(ref) {
			var deferred = $q.defer();
			$http["delete"]("/api/persons/" + ref)
				.success(function(data) {
					deferred.resolve(data);
				})
				.error(function(data) {
					deferred.reject(data);
				});

			return deferred.promise;
		};
	});

}());
