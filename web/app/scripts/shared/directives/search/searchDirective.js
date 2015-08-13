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

    $scope.toText = function (result) {
      var item = result.item;
      if (result.type === "person") {
        return item.firstName + " " + item.lastName;
      } else if (result.type === "map") {
        return item.name;
      }
    };

    $scope.onSelect = function (result) {
      if (result.type === "person") {
        $location.path("/people/" + result.ref);
      } else if (result.type === "map") {
        $location.path("/maps/" + result.ref);
      }
		};
	});

	app.directive("search", function() {
		return {
			restrict: "E",
      templateUrl: "scripts/shared/directives/search/searchView.html"
		};
	});

}());
