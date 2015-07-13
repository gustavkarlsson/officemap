/* global alert */
/* global angular */

(function () {
  "use strict";
  var app = angular.module("main");

  app.controller("EditMapController", function ($scope, $state, ref, map, MapService, DiffService, ImageService) {
    // Variables
    var originalMap;

    // Static
    originalMap = map ? angular.copy(map) : {};

    // Scope
    $scope.ref = ref;
    $scope.map = angular.copy(originalMap);
    $scope.isNew = originalMap === {}

    $scope.isChanged = function () {
      return !angular.equals($scope.map, originalMap);
    };

    $scope.revert = function () {
      $scope.map = angular.copy(originalMap);
    };

    $scope.create = function () {
      MapService.create($scope.map)
        .then(function (ref) {
          $state.go("admin.home", { tab: "maps" });
          //TODO toast success
        }, function (reason) {
          alert("Failed: " + reason);
          //TODO toast failed
        });
    };

    $scope.update = function () {
      var changes = DiffService.getChanges(originalMap, $scope.map);
      MapService.update($scope.ref, changes)
        .then(function () {
          $state.go("admin.home", { tab: "maps" });
          //TODO toast success
        }, function (reason) {
          alert("Failed: " + reason);
          //TODO toast failed
        });
    };

    $scope.remove = function () {
      // TODO add modal with confirmation
      MapService.remove($scope.ref)
        .then(function () {
          $state.go("admin.home", { tab: "maps" });
          //TODO toast success
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
      return "/api/files/" + $scope.map.image;
    };

    // Listeners
    $scope.$watch("file", function () {
      if (!$scope.file) {
        return;
      }
      var promise = ImageService.upload($scope.file[0]);
      promise.then(function (sha1) {
        $scope.map.image = sha1;
      }, function (reason) {
        alert("Failed: " + reason);
        //TODO toast failed
      });
    });
  });
}());
