/* global alert */
/* global angular */

(function () {
  "use strict";
  var app = angular.module("main");

  app.controller("EditMapController", function ($scope, $state, $mdToast, ref, map, MapService, DiffService, ImageService) {
    // Variables
    var originalMap,
      hasFiles;

    // Static
    originalMap = map ? angular.copy(map) : {};

    // Functions
    hasFiles = function () {
      return $scope.files && $scope.files.length > 0;
    };

    // Scope
    $scope.map = angular.copy(originalMap);
    $scope.isNew = angular.equals(originalMap, {});

    $scope.isChanged = function () {
      return !angular.equals($scope.map, originalMap);
    };

    $scope.revert = function () {
      $scope.map = angular.copy(originalMap);
    };

    $scope.create = function () {
      MapService.create($scope.map)
        .then(function () {
          $state.go("admin.home", { tab: "maps" });
          $mdToast.show($mdToast.simple().position("bottom right").content("Created " + $scope.map.name));
        }, function (reason) {
          alert("Failed: " + reason);
          //TODO toast failed
        });
    };

    $scope.update = function () {
      var changes = DiffService.getChanges(originalMap, $scope.map);
      MapService.update(ref, changes)
        .then(function () {
          $state.go("admin.home", { tab: "maps" });
          $mdToast.show($mdToast.simple().position("bottom right").content("Updated " + $scope.map.name));
        }, function (reason) {
          alert("Failed: " + reason);
          //TODO toast failed
        });
    };

    $scope.remove = function () {
      // TODO add modal with confirmation
      MapService.remove(ref)
        .then(function () {
          $state.go("admin.home", { tab: "maps" });
          $mdToast.show($mdToast.simple().position("bottom right").content("Deleted " + $scope.map.name));
        }, function (reason) {
          alert("Failed: " + reason);
          //TODO toast failed
        });
    };

    $scope.hasImage = function () {
      return $scope.map.image;
    };

    $scope.getImageUrl = function () {
      if (!$scope.map.image) {
        return "/images/map.png";
      }
      return ImageService.getUrl($scope.map.image, 320);
    };

    // Listeners
    $scope.$watch("files", function () {
      if (hasFiles()) {
        ImageService.upload($scope.files[0])
          .then(function (sha1) {
            $scope.map.image = sha1;
          }, function (reason) {
            alert("Failed: " + reason);
            //TODO toast failed
          });
      }
    });
  });
}());
