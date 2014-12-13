(function () {
	var app = angular.module("main", ["ui.bootstrap", "ngRoute", "leaflet-directive"]);

	app.factory("AreaService", function ($http, $q) {
		
		// Members
		var active = undefined;
		
		// Methods
		return {
			getArea: function (ref) {
				var deferred = $q.defer();
				$http.get("api/areas/" + ref)
				.success(function(data, status) {
					deferred.resolve(data);
				})
				.error(function(data, status) {
					deferred.reject(data);
				});

				return deferred.promise;
			},
			getAreas: function () {
				var deferred = $q.defer();
				$http.get("api/areas")
				.success(function(data, status) {
					deferred.resolve(data);
				})
				.error(function(data, status) {
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
			getDimensions: function (url) {
				var deferred = $q.defer();
				var image = new Image();
				
				image.onload = function () {
					deferred.resolve([this.width, this.height])
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
		
		$scope.leafletData = leafletData;
		
		// Init
		AreaService.setActive(area);
		var mapUrl = "api/file/" + area.map;
		
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
		
		var dimensionsPromise = ImageService.getDimensions("api/file/" + area.map);
		dimensionsPromise.then(
			function (dimensions) {
				$scope.leafletData.getMap().then( function (map) {
					map.eachLayer(function (layer) {
						map.removeLayer(layer);
					});
					var bounds = [[-(dimensions[1]/2), -(dimensions[0]/2)], [(dimensions[1]/2), (dimensions[0]/2)]];
					L.imageOverlay(mapUrl, bounds).addTo(map).bringToFront();
				});
			},
			function (reason) {
				alert("Failed: " + reason);
			}
		);
	});

	app.config(function ($routeProvider) {
		$routeProvider
		.when("/areas/:reference", {
			templateUrl: "partials/area.html",
			controller: "MapController",
			resolve: {
				area: function (AreaService, $route) {
					return AreaService.getArea($route.current.params.reference);
				}
			}
		})
		.when("/persons/:reference", {
			templateUrl: "partials/person.html",
			controller: "PersonController",
			resolve: {
				person: function (PersonService, $route) {
					return PersonService.getPerson($route.current.params.reference);
				}
			}
		})
		.otherwise({
			redirectTo: "/"
		});
	});

	app.factory("PersonService", function ($http, $q) {
		return {
			getPerson: function (reference) {
				return $http.get("/api/persons/" + reference).then(
					function (response) {
						if (typeof response.data === "object") {
							return response.data;
						} else {
							// invalid response
							return $q.reject(response.data);
						}

					}, function (response) {
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

})();