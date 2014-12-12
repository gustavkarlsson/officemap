(function() {
	var app = angular.module("main", [ "ui.bootstrap", "ngRoute",
			"leaflet-directive" ]);

	app.controller("TypeaheadController", function($scope, $http) {

		$scope.selected = undefined;

		$scope.getPersons = function(searchTerm) {
			// TODO add more effective searching
			return $http.get("/api/persons").then(function(response) {
				return response.data;
			});
		};

		$scope.onSelect = function($item, $model, $label) {
			window.location.href = "#/persons/" + $item.reference;
		};
	});

	app.controller("PersonController", [ "$scope", "person",
			function($scope, person) {
				$scope.person = person;
			} ]);

	app.controller("MapController", [ "$scope", function($scope) {
		angular.extend($scope, {
			defaults : {
				zoomControl : false,
                crs: 'Simple',
                maxZoom: 2
			},
			controls : {
				custom : [ new L.Control.Zoom({
					position : "bottomright"
				}) ]
			},
			center: {
                lat: 0,
                lng: 0,
                zoom: 0
            },
			layers: {
	            baselayers: {
	                andes: {
	                    name: 'Andes',
	                    type: 'imageOverlay',
	                    url: 'http://tombatossals.github.io/angular-leaflet-directive/examples/img/andes.jpg',
	                    bounds: [[-540, -960], [540, 960]],
	                    layerParams: {
	                      noWrap: true
	                    }
	                }
	            }
	        }
		});
	} ]);

	app.factory("PersonService", function($http, $q) {
		return {
			getPerson : function(reference) {
				return $http.get("/api/persons/" + reference).then(
						function(response) {
							if (typeof response.data === "object") {
								return response.data;
							} else {
								// invalid response
								return $q.reject(response.data);
							}

						}, function(response) {
							// something went wrong
							return $q.reject(response.data);
						});
			}
		};
	});

	app
			.config([
					"$routeProvider",
					function($routeProvider) {
						$routeProvider
								.when(
										"/persons/:reference",
										{
											templateUrl : "partials/person.html",
											controller : "PersonController",
											resolve : {
												person : function(
														PersonService, $route) {
													return PersonService
															.getPerson($route.current.params.reference);
												}
											}
										}).otherwise({
									redirectTo : "/"
								});
					} ]);

})();