/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("AdminController", function($scope, $location, ImageService, persons, maps) {
		$scope.persons = persons;
		$scope.maps = maps;

    $scope.go = function(path) {
      $location.path(path);
    };

    $scope.getThumbnail = function(hash, size) {
      var sizeQuery = "";

      if (!hash) {
        return "#";
      }
      if (size) {
        sizeQuery = "?size=" + size;
      }
      return "/api/files/" + hash + sizeQuery;
    };

	});

}());
