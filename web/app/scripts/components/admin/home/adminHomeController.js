/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("AdminHomeController", function($rootScope, $scope, $state, $location, persons, maps, initialTab) {
    var tabs, getInitialTab;

    tabs = [ "people", "maps" ];

    getInitialTab = function() {
      if (initialTab) {
        return tabs.indexOf(initialTab);
      }
      return 0;
    }

    $rootScope.$state = $state;
    $scope.persons = persons;
		$scope.maps = maps;
    $scope.tab = getInitialTab();

    $scope.$watch("tab", function(index){
      var tab = tabs[index];
      if (tab) {
        $location.search("tab", tab);
      }
    });

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
