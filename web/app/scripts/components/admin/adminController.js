/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("AdminController", function($scope, $state, ImageService, persons, maps, section) {

    var getSectionId = function(section) {
      if (section === 'people') {
        return 0;
      }
      if (section === 'maps') {
        return 1;
      }
      return 0;
    };

		$scope.persons = persons;
		$scope.maps = maps;
    $scope.sectionId = getSectionId(section);

    $scope.updateSection = function(newSection) {
      if ($state.params.section || getSectionId(newSection) !== 0) {
        $state.transitionTo('.', { section: newSection }, {
          location: true,
          inherit: true,
          relative: $state.$current,
          notify: false
        });
      }
    };

    $scope.getThumbnail = function(sha1, size) {
      var sizeQuery = "";

      if (!sha1) {
        return "#";
      }
      if (size) {
        sizeQuery = "?size=" + size;
      }
      return "/api/files/" + sha1 + sizeQuery;
    };

	});

}());
