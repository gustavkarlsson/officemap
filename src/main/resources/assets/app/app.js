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

	app.controller("MapController", function ($scope, $http, $q, AreaService, area) {
		
		// Init
		AreaService.setActive(area);
		var mapUrl = "api/file/" + area.map;
		var bounds = [[-540, -960], [540, 960]]; // TODO get bounds programatically
		
		var getBounds = function (area) {
			
		};
		
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
					andes: {
						name: "Andes",
						type: "imageOverlay",
						url: mapUrl,
						bounds: bounds,
						layerParams: {
							noWrap: true
						}
					}
				}
			}
		});
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