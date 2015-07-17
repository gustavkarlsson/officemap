/* global alert */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("EditPersonController", function($scope, $state, $mdToast, $mdDialog, ref, person, PersonService, DiffService, ImageService) {
    // Variables
    var originalPerson,
      hasFiles;

		// Static
		originalPerson = person ? angular.copy(person) : {};

    // Functions
    hasFiles = function () {
      return $scope.files && $scope.files.length > 0;
    };

    // Scope
		$scope.person = angular.copy(originalPerson);
    $scope.isNew = angular.equals(originalPerson, {});

		$scope.isChanged = function() {
			return !angular.equals($scope.person, originalPerson);
		};

		$scope.revert = function() {
			$scope.person = angular.copy(originalPerson);
		};

    $scope.getFullName = function() {
      var fullName = "";
      if ($scope.person.firstName) {
        fullName = $scope.person.firstName;
        if ($scope.person.lastName) {
          fullName = fullName + " " + $scope.person.lastName;
        }
      }
      return fullName;
    };

		$scope.create = function() {
			PersonService.create($scope.person)
        .then(function() {
          $state.go("admin.home", { tab: "people" });
          $mdToast.show($mdToast.simple().position("bottom right").content("Created " + $scope.getFullName()));
        }, function(reason) {
          alert("Failed: " + reason);
          //TODO toast failed
        });
		};

		$scope.update = function() {
			var changes = DiffService.getChanges(originalPerson, $scope.person);
			PersonService.update(ref, changes)
        .then(function() {
          $state.go("admin.home", { tab: "maps" });
          $mdToast.show($mdToast.simple().position("bottom right").content("Updated " + $scope.getFullName()));
        }, function(reason) {
          alert("Failed: " + reason);
          //TODO toast failed
        });
		};

		$scope.remove = function() {
      // TODO add modal with confirmation
			PersonService.remove(ref)
        .then(function() {
          $state.go("admin.home", { tab: "maps" });
          $mdToast.show($mdToast.simple().position("bottom right").content("Deleted " + $scope.getFullName()));
				}, function(reason) {
					alert("Failed: " + reason);
					//TODO toast failed
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
			return ImageService.getUrl($scope.person.portrait, 160);
		};

    $scope.getLocationName = function() {
      if (!$scope.person.location) {
        return "No location";
      }
      return $scope.person.location.name;
    };

    $scope.showSelectLocationDialog = function($event) {
      $mdDialog.show({
        targetEvent: $event,
        locals: {
          initialLocation: $scope.person.location
        },
        resolve: {
          maps: function (MapService) {
            return MapService.getAll();
          }
        },
        controller: DialogController,
        template:
        '<md-dialog aria-label="List dialog">'+
        '   <md-toolbar>'+
        '   <div class="md-toolbar-tools">'+
        '     <h2>Select location</h2>'+
        '     <span flex></span>'+
        '     <md-button class="md-icon-button" ng-click="cancel()">'+
        '       <md-icon aria-label="Cancel">close</md-icon>'+
        '     </md-button>'+
        '   </div>'+
        ' </md-toolbar>'+
        '  <md-dialog-content>'+
        '    <select-location maps="maps" location="location"></select-location>'+
        '  </md-dialog-content>'+
        '  <div class="md-actions">'+
        '    <md-button ng-click="confirm()" class="md-primary">'+
        '      OK'+
        '    </md-button>'+
        '    <md-button ng-click="cancel()">'+
        '      Cancel'+
        '    </md-button>'+
        '  </div>'+
        '</md-dialog>'
      }).then(
        function(location) {
          $scope.person.location = location;
        }
      );
      function DialogController($scope, $mdDialog, initialLocation ,maps) {
        $scope.location = initialLocation;
        $scope.maps = maps;
        $scope.confirm = function() {
          $mdDialog.hide($scope.location);
        };
        $scope.cancel = function() {
          $mdDialog.cancel();
        };
      }
    };

    // Listeners
    $scope.$watch("files", function () {
      if (hasFiles()) {
        ImageService.upload($scope.files[0])
          .then(function (sha1) {
            $scope.person.portrait = sha1;
          }, function (reason) {
            alert("Failed: " + reason);
            //TODO toast failed
          });
      }
		});
	});
}());
