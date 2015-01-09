/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main", ["ui.bootstrap", "ngRoute", "leaflet-directive"]);

	app.factory("MapService", function ($http, $q) {
		
		// Members
		var active;
		
		// Methods
		return {
			getMap: function (ref) {
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
			getMaps: function () {
				var deferred = $q.defer();
				$http.get("api/maps/")
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

	app.controller("MapsController", function ($scope, $http, MapService) {
		
		// Init
		var mapsPromise = MapService.getMaps();
		mapsPromise.then(
			function (maps) {
				$scope.maps = maps;
			},
			function (reason) {
				alert("Failed: " + reason);
			}
		);
		
		// Members
		$scope.maps = [];
		
		// Methods
		$scope.isActive = function (ref) {
			var active = MapService.getActive();
			return typeof active === "undefined" ? false : ref === active;
		};
		
	});

	app.controller("LeafletController", function ($scope, MapService, ImageService, leafletData, ref, map) {
		
		// Variable declarations
		var getLeafletMap, getImageBounds, offsetBounds;
		
		// Init
		MapService.setActive(ref);
		getLeafletMap = leafletData.getMap;
		
		// Methods
		getImageBounds = function (img) {
			return [[-(img.height / 2), -(img.width / 2)], [img.height / 2, img.width / 2]];
		};
		
		offsetBounds = function (bounds, offset) {
			return [[bounds[0][0] - offset, bounds[0][1] - offset], [bounds[1][0] + offset, bounds[1][1] + offset]];
		};
		
		// Build Leaflet Map
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
		getLeafletMap().then(function (leafletMap) {
			leafletMap.eachLayer(function (layer) {
				leafletMap.removeLayer(layer);
			});
		});
		
		// Get dimensions of map and add map layer
		ImageService.load("api/files/" + map.image).then(
			function (img) {
				getLeafletMap().then(function (leafletMap) {
					var bounds, maxBounds;
					bounds = getImageBounds(img);
					maxBounds = offsetBounds(bounds, 200);
					L.imageOverlay("api/files/" + map.image, bounds).addTo(leafletMap).bringToFront();
					leafletMap.setMaxBounds(maxBounds);
				});
			},
			function (reason) {
				alert("Failed: " + reason);
			}
		);
	});

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