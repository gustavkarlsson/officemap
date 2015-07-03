/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("EditMapController", function ($scope, $location, $route, ref, map, MapService, DiffService, ImageService, ModalService) {
		var originalMap = angular.copy(map);

		$scope.ref = ref;
		$scope.map = map;
		if ($scope.map === null) {
			$scope.map = {};
		}

		$scope.isChanged = function () {
			return !angular.equals($scope.map, originalMap);
		};

		$scope.revert = function () {
			$scope.map = angular.copy(originalMap);
		};

		$scope.isNew = function () {
			return originalMap === null;
		};

		$scope.create = function () {
			var promise = MapService.create($scope.map);
			promise.then(function (ref) {
				$location.path("/admin");
				//TODO toast success
			}, function (reason) {
				alert("Failed: " + reason);
				//TODO toast failed
			});
		};

		$scope.update = function () {
			var changes = DiffService.getChanges(originalMap, $scope.map),
				promise;
			promise = MapService.update($scope.ref, changes);
			promise.then(function () {
				$location.path("/admin");
				//TODO toast success
			}, function (reason) {
				alert("Failed: " + reason);
				//TODO toast failed
			});
		};

		$scope.remove = function () {
			var modalOptions = {
				bodyText: "Are you sure you want to delete this map?"
			};

			ModalService.showDeleteModal({}, modalOptions).then(function (result) {
				var promise = MapService.remove($scope.ref);
				promise.then(function () {
					$location.path("/admin");
					//TODO toast success
				}, function (reason) {
					alert("Failed: " + reason);
					//TODO toast failed
				});
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
