/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.config(function ($routeProvider) {
		$routeProvider
			.when("/", {
				templateUrl: "/app/components/map/mapView.html",
				controller: "LeafletController",
				resolve: {
					mapRef: function () {
						return null;
					},
					map: function () {
						return null;
					}
				}
			})
			.when("/maps/:ref", {
				templateUrl: "/app/components/map/mapView.html",
				controller: "LeafletController",
				resolve: {
					mapRef: function ($route) {
						return $route.current.params.ref;
					},
					map: function (MapService, $route) {
						return MapService.get($route.current.params.ref);
					}
				}
			})
			.when("/admin", {
				templateUrl: "/app/components/admin/adminView.html",
				controller: "AdminController",
				resolve: {
					persons: function (PersonService) {
						return PersonService.getAll();
					},
					maps: function (MapService) {
						return MapService.getAll();
					}
				}
			})
			.when("/admin/persons/new", {
				templateUrl: "/app/components/createPerson/createPersonView.html",
				controller: "CreatePersonController"
			})
			.when("/admin/persons/:ref", {
				templateUrl: "/app/components/editPerson/editPersonView.html",
				controller: "EditPersonController",
				resolve: {
					ref: function ($route) {
						return $route.current.params.ref;
					},
					person: function (PersonService, $route) {
						return PersonService.get($route.current.params.ref);
					}
				}
			})
			.otherwise({
				redirectTo: "/"
			});
	});

}());