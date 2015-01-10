/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.config(function ($routeProvider) {
		$routeProvider
			.when("/maps/:ref", {
				templateUrl: "partials/map.html",
				controller: "LeafletController",
				resolve: {
					ref: function ($route) {
						return $route.current.params.ref;
					},
					map: function (MapService, $route) {
						return MapService.getMap($route.current.params.ref);
					}
				}
			})
			.when("/persons/:ref", {
				templateUrl: "partials/person.html",
				controller: "PersonController",
				resolve: {
					person: function (PersonService, $route) {
						return PersonService.getPerson($route.current.params.ref);
					}
				}
			})
			.otherwise({
				redirectTo: "/"
			});
	});

}());