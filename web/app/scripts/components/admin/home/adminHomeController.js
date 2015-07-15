/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("AdminHomeController", function($scope, $state, $location, persons, maps, initialTab, ImageService) {
    // Variables
    var tabs,
      getInitialTab;

    // Static
    tabs = [ "people", "maps" ];
    getInitialTab = function() {
      if (initialTab) {
        return tabs.indexOf(initialTab);
      }
      return 0;
    };

    // Scope
    $scope.persons = persons;
    $scope.maps = maps;
    $scope.tab = getInitialTab();
    $scope.go = $state.go;

    $scope.getPortraitThumbnailUrl = function(person) {
      if (!person.portrait) {
        return "/images/profile.png";
      }
      return ImageService.getUrl(person.portrait, 40);
    };

    $scope.getMapImageThumbnailUrl = function(map) {
      if (!map.image) {
        return "/images/map.png";
      }
      return ImageService.getUrl(map.image, 80);
    };

    // Listeners
    $scope.$watch("tab", function(index){
      var tab = tabs[index];
      if (tab) {
        $location.search("tab", tab);
      }
    });
	});
}());
