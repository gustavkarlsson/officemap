/* global alert */
/* global angular */
/* global L */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("SelectLocationController", function($scope, ImageService, leafletData) {
    // Variables
    var leafletMap,
      positionMarker,
      getImageBounds,
      updateMarker;

    // Static
    leafletData.getMap().then(function(m) {
      leafletMap = m;
      leafletMap.on("click", function(event) {
        updateMarker(event.latlng.lat, event.latlng.lng);
      });
      $scope.setMap($scope.maps[$scope.$parent.location.mapRef]);
    });

    //Variables
    positionMarker = null;

    // Functions
    getImageBounds = function(img) {
      return [
        [-(img.height / 2), -(img.width / 2)],
        [img.height / 2, img.width / 2]
      ];
    };

    updateMarker = function (latitude, longitude) {
      if (latitude === undefined || latitude === null || longitude === undefined || longitude === null) {
        return;
      }
      if (positionMarker === null) {
        positionMarker = L.marker([latitude, longitude], {clickable: false});
      } else {
        positionMarker.setLatLng([latitude, longitude]);
      }
      positionMarker.addTo(leafletMap);
      $scope.$parent.location.latitude = latitude;
      $scope.$parent.location.longitude = longitude;
    };

    $scope.setMap = function (map) {
      ImageService.get(map.image).then(function(image) {
        leafletData.getMap().then(function(leafletMap) {
          var bounds = getImageBounds(image);
          leafletMap.eachLayer(function(layer) {
            leafletMap.removeLayer(layer);
          });
          new L.ImageOverlay( "/api/files/" + map.image, bounds).addTo(leafletMap).bringToFront();
          leafletMap.fitBounds(bounds);
          updateMarker($scope.$parent.location.latitude, $scope.$parent.location.longitude);
        });
      });
    };

    // Scope
    angular.extend($scope, {
      defaults: {
        zoomControl: false,
        crs: "Simple",
        maxZoom: 2
      },
      controls: {
        custom: [new L.Control.Zoom({
          position: "bottomright"
        })]
      },
      center: {
        lat: 0,
        lng: 0,
        zoom: 0
      },
      layers: {
        baselayers: {
          map: {
            name: "map",
            type: "imageOverlay",
            url: "/images/nothing.png",
            bounds: [ [0, 0], [0, 0]]
          }
        }
      }
    });

    $scope.hasMap = function() {
      var location = $scope.$parent.location;
      return location !== undefined && location !== null && location.mapRef !== undefined && location.mapRef !== null;
    };
	});

	app.directive("selectLocation", function() {
		return {
			restrict: "E",
      templateUrl: "scripts/shared/directives/selectLocation/selectLocationView.html",
      transclude: true
		};
	});

}());
