/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.config(function ($routeProvider) {
		$routeProvider
			.when("/", {
				templateUrl: "/app/components/home/homeView.html"
			})
			.when("/maps/:ref", {
				templateUrl: "/app/components/map/mapView.html",
				controller: "MapController",
				resolve: {
					mapRef: function ($route) {
						return $route.current.params.ref;
					},
					map: function (MapService, $route) {
						return MapService.get($route.current.params.ref);
					},
					image: function (MapService, ImageService, $route) {
						return MapService.get($route.current.params.ref).then(function (map) {
                            return ImageService.get(map.image);
                        });
					},
					persons: function (PersonService, $route) {
						return PersonService.getAllByMapRef($route.current.params.ref);
					},
					activePersonRef: function () {
						return null;
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
			.when("/admin/maps/new", {
				templateUrl: "/app/components/editMap/editMapView.html",
				controller: "EditMapController",
				resolve: {
					ref: function ($route) {
						return null;
					},
					map: function (MapService, $route) {
						return null;
					}
				}
			})
			.when("/admin/maps/:ref", {
				templateUrl: "/app/components/editMap/editMapView.html",
				controller: "EditMapController",
				resolve: {
					ref: function ($route) {
						return $route.current.params.ref;
					},
					map: function (MapService, $route) {
						return MapService.get($route.current.params.ref);
					}
				}
			})
			.when("/admin/persons/new", {
				templateUrl: "/app/components/editPerson/editPersonView.html",
				controller: "EditPersonController",
				resolve: {
					ref: function ($route) {
						return null;
					},
					person: function (PersonService, $route) {
						return null;
					}
				}
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
