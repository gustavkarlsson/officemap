/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.service("MapService", function ($http, $q) {

        this.get = function (ref) {
            var deferred = $q.defer();
            $http.get("/api/maps/" + ref)
                .success(function (data, status) {
                    deferred.resolve(data);
                })
                .error(function (data, status) {
                    deferred.reject(data);
                });

            return deferred.promise;
        };

        this.getAll = function () {
            var deferred = $q.defer();
            $http.get("/api/maps/")
                .success(function (data, status) {
                    deferred.resolve(data);
                })
                .error(function (data, status) {
                    deferred.reject(data);
                });

            return deferred.promise;
        };

        this.create = function (map) {
            var deferred = $q.defer();
            $http.post("/api/maps/", map)
                .success(function (ref) {
                    deferred.resolve(ref);
                })
                .error(function (data, status) {
                    deferred.reject(data);
                });

            return deferred.promise;
        };

        this.update = function (ref, changes) {
            var deferred = $q.defer();
            $http.patch("/api/maps/" + ref, changes)
                .success(function (data, status) {
                    deferred.resolve(data);
                })
                .error(function (data, status) {
                    deferred.reject(data);
                });

            return deferred.promise;
        };

        this.remove = function (ref) {
            var deferred = $q.defer();
            $http["delete"]("/api/maps/" + ref)
                .success(function (data, status) {
                    deferred.resolve(data);
                })
                .error(function (data, status) {
                    deferred.reject(data);
                });

            return deferred.promise;
        };

	});

}());