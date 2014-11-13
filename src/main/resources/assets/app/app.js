(function () {
	var app = angular.module("main", ["ui.bootstrap", "ngRoute"]);

	app.controller("TypeaheadCtrl", function ($scope, $http) {

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

	app.controller("PersonCtrl", ["$scope", "person",
		function ($scope, person) {
			$scope.person = person;
	}]);

	app.factory("PersonService", function ($http, $q) {
		return {
			getPerson: function (reference) {
				return $http.get("/api/persons/" + reference)
					.then(function (response) {
						if (typeof response.data === "object") {
							return response.data;
						} else {
							// invalid response
							return $q.reject(response.data);
						}

					}, function (response) {
						// something went wrong
						return $q.reject(response.data);
					});
			}
		};
	});

	app.config(["$routeProvider",
		function ($routeProvider) {
			$routeProvider.
			when("/persons/:reference", {
				templateUrl: "partials/person.html",
				controller: "PersonCtrl",
				resolve: {
					person: function (PersonService, $route) {            
						return PersonService.getPerson($route.current.params.reference);
					}
				}
			}).
			otherwise({
				redirectTo: "/"
			});
		}]);

})();