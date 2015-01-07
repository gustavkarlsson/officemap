/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main", ["ui.bootstrap", "ngRoute", "leaflet-directive"]);

	app.factory("AreaService", function ($http, $q) {
		
		// Members
		var active;
		
		// Methods
		return {
			getArea: function (ref) {
				var deferred = $q.defer();
				$http.get("api/maps/" + ref)
					.success(function (data, status) {
						deferred.resolve(data);
					})
					.error(function (data, status) {
						deferred.reject(data);
					});

				return deferred.promise;
			},
			getAreas: function () {
				var deferred = $q.defer();
				$http.get("api/maps")
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
			setActive: function (area) {
				active = area;
				return;
			}
		};
		
	});

	app.factory("ImageService", function ($q) {
		
		// Methods
		return {
			load: function (url) {
				var deferred, image;
				deferred = $q.defer();
				image = new Image();
				
				image.onload = function () {
					deferred.resolve(this);
				};
				image.error = function () {
					deferred.reject(false);
				};

				image.src = url;
				return deferred.promise;
			}
		};
		
	});

	app.controller("AreasController", function ($scope, $http, AreaService) {
		
		// Init
		var areasPromise = AreaService.getAreas();
		areasPromise.then(
			function (areas) {
				$scope.areas = areas;
			},
			function (reason) {
				alert("Failed: " + reason);
			}
		);
		
		// Members
		$scope.areas = [];
		
		// Methods
		$scope.isActive = function (area) {
			var active = AreaService.getActive();
			return typeof active === "undefined" ? false : area.reference === active.reference;
		};
		
	});

	app.controller("MapController", function ($scope, AreaService, ImageService, leafletData, area) {
		
		// Variable declarations
		var getMap, getImageBounds, offsetBounds;
		
		// Init
		AreaService.setActive(area);
		getMap = leafletData.getMap;
		
		// Methods
		getImageBounds = function (img) {
			return [[-(img.height / 2), -(img.width / 2)], [img.height / 2, img.width / 2]];
		};
		
		offsetBounds = function (bounds, offset) {
			return [[bounds[0][0] - offset, bounds[0][1] - offset], [bounds[1][0] + offset, bounds[1][1] + offset]];
		};
		
		// Build map
		angular.extend($scope, {
			defaults: {
				zoomControl: false,
				crs: "Simple",
				maxZoom: 2
			},
			controls: {
				custom: [new L.Control.Zoom({
					position: "bottomright"
				})]
			},
			center: {
				lat: 0,
				lng: 0,
				zoom: 0
			},
			layers: {
				baselayers: {
					dummy: {
						name: "dummy",
						type: "imageOverlay",
						url: "images/dummy_map.png",
						bounds: [[0, 0], [0, 0]],
						layerParams: {
							noWrap: true
						}
					}
				}
			}
		});
		
		// Clear layers
		getMap().then(function (map) {
			map.eachLayer(function (layer) {
				map.removeLayer(layer);
			});
		});
		
		// Get dimensions of map and add map layer
		ImageService.load("api/files/" + area.image).then(
			function (img) {
				getMap().then(function (map) {
					var bounds, maxBounds;
					bounds = getImageBounds(img);
					maxBounds = offsetBounds(bounds, 200);
					L.imageOverlay("api/files/" + area.image, bounds).addTo(map).bringToFront();
					map.setMaxBounds(maxBounds);
				});
			},
			function (reason) {
				alert("Failed: " + reason);
			}
		);
		
		area.persons.forEach(function (personRef) {
			var map = getMap();
		});
	});

	app.config(function ($routeProvider) {
		$routeProvider
			.when("/maps/:ref", {
				templateUrl: "partials/area.html",
				controller: "MapController",
				resolve: {
					area: function (AreaService, $route) {
						return AreaService.getArea($route.current.params.ref);
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
			}
		};
	});

	app.controller("PersonController", function ($scope, person) {
		$scope.person = person;
	});

	app.controller("TypeaheadController", function ($scope, $http) {

		$scope.selected = undefined;

		$scope.getPersons = function (searchTerm) {
			// TODO add more effective searching
			return $http.get("/api/persons").then(function (response) {
				return response.data;
			});
		};

		$scope.onSelect = function ($item, $model, $label) {
			window.location.href = "#/persons/" + $item.reference;
		};
	});

}());