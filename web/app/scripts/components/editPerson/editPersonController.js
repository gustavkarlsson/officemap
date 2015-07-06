/* global alert */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("EditPersonController", function($scope, $location, $route, ref, person, PersonService, MapService,
		DiffService, ImageService, ModalService) {
		// Init
		var originalPerson = angular.copy(person);
		$scope.ref = ref;
		$scope.person = person;
		if ($scope.person === null) {
			$scope.person = {};
		}
		MapService.getAll().then(
			function(maps) {
				$scope.maps = maps;
			},
			function(reason) {
				alert("Failed: " + reason);
			}
		);

		// Functions
		$scope.isChanged = function() {
			return !angular.equals($scope.person, originalPerson);
		};

		$scope.revert = function() {
			$scope.person = angular.copy(originalPerson);
		};

		$scope.isNew = function() {
			return originalPerson === null;
		};

		$scope.create = function() {
			var promise = PersonService.create($scope.person);
			promise.then(function(ref) {
				$location.path("/admin");
				//TODO toast success
			}, function(reason) {
				alert("Failed: " + reason);
				//TODO toast failed
			});
		};

		$scope.update = function() {
			var changes = DiffService.getChanges(originalPerson, $scope.person),
				promise;
			promise = PersonService.update($scope.ref, changes);
			promise.then(function() {
				$location.path("/admin");
				//TODO toast success
			}, function(reason) {
				alert("Failed: " + reason);
				//TODO toast failed
			});
		};

		$scope.remove = function() {
			var modalOptions = {
				bodyText: "Are you sure you want to delete this person?"
			};

			ModalService.showDeleteModal({}, modalOptions).then(function(result) {
				var promise = PersonService.remove($scope.ref);
				promise.then(function() {
					$location.path("/admin");
					//TODO toast success
				}, function(reason) {
					alert("Failed: " + reason);
					//TODO toast failed
				});
			});
		};

		$scope.removePortrait = function() {
			$scope.person.portrait = null;
		};

		$scope.hasPortrait = function() {
			return $scope.person.portrait;
		};

		$scope.getPortraitUrl = function() {
			if (!$scope.person.portrait) {
				return "/images/profile.png";
			}
			return "/api/files/" + $scope.person.portrait;
		};

		$scope.updateLocation = function() {
			if (!$scope.person.location.mapRef) {
				$scope.person.location = null;
			}
		};

		$scope.$watch("file", function() {
			if (!$scope.file) {
				return;
			}
			var promise = ImageService.upload($scope.file[0]);
			promise.then(function(sha1) {
				$scope.person.portrait = sha1;
			}, function(reason) {
				alert("Failed: " + reason);
				//TODO toast failed
			});
		});
	});
}());
