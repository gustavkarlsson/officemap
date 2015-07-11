/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("SearchController", function($scope, $location, $http, $q) {

		$scope.search = function(searchTerm) {
			var q = $q.defer();
			$http.post("/api/search", searchTerm).then(function(response) {
				q.resolve(response.data);
			});
			return q.promise;
		};

		$scope.onSelect = function($item) {
			if ($item.object.location) {
				$location.path("/people/" + $item.ref);
			}
		};
	});

	app.directive("search", function() {
		return {
			restrict: "E",
			templateUrl: "/scripts/shared/directives/search/searchView.html"
		};
	});

}());
