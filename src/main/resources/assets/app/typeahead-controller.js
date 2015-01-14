/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("TypeaheadController", function ($scope, $http) {

		$scope.selected = undefined;

		$scope.search = function (searchTerm) {
			return $http.post("/api/search", searchTerm).then(function (response) {
				return response.data;
			});
		};

		$scope.onSelect = function ($item, $model, $label) {
			window.location.href = $item.url;
		};
	});

}());