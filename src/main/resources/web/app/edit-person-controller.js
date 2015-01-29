/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("EditPersonController", function ($scope, $location, $route, ref, person, PersonService, DiffService, ImageService) {
		var originalPerson = angular.copy(person);

		$scope.ref = ref;
		$scope.person = person;

		$scope.removePortrait = function () {
			$scope.person.portrait = null;
		};

		$scope.hasPortrait = function () {
			return $scope.person.portrait;
		};

		$scope.isChanged = function () {
			return !angular.equals($scope.person, originalPerson);
		};

		$scope.save = function () {
			var changes = DiffService.getChanges(originalPerson, $scope.person),
				promise;
			promise = PersonService.update($scope.ref, changes);
			promise.then(function () {
				$route.reload();
				//TODO toast success
			}, function (reason) {
				alert('Failed: ' + reason);
				//TODO toast failed
			});
		};

		$scope.reset = function () {
			$scope.person = angular.copy(originalPerson);
		};

		$scope.delete = function () {
			var promise = PersonService.delete($scope.ref);
			promise.then(function () {
				$location.path("/admin");
				//TODO toast success
			}, function (reason) {
				alert('Failed: ' + reason);
				//TODO toast failed
			});
		};

		$scope.getPortraitUrl = function () {
			if (!$scope.person.portrait) {
				return "/images/profile.png";
			}
			return "/api/files/" + $scope.person.portrait;
		};

		$scope.$watch("file", function () {
			if (!$scope.file) {
				return;
			}
			var promise = ImageService.upload($scope.file[0]);
			promise.then(function (sha1) {
				$scope.person.portrait = sha1;
			}, function (reason) {
				alert('Failed: ' + reason);
				//TODO toast failed
			});
		});
	});
}());