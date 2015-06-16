/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("SearchController", function ($scope, $location, $http) {

        $scope.selected = undefined;

        $scope.search = function (searchTerm) {
            return $http.post("/api/search", searchTerm).then(function (response) {
                return response.data;
            });
        };

        $scope.onSelect = function ($item, $model, $label) {
            $location.path($item.url);
        };
    });

    app.directive("search", function() {
        return {
            restrict: "E",
            templateUrl: "/app/shared/directives/search/searchView.html"
        };
    });

}());