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
      offsetBounds,
      setMap;

    // Static
    leafletData.getMap().then(function(m) {
      leafletMap = m;
      leafletMap.on("click", function(event) {
        if (positionMarker === null) {
          positionMarker = new L.marker(event.latlng, { clickable: false });
        } else {
          positionMarker.setLatLng(event.latlng);
        }
        positionMarker.addTo(leafletMap);
        $scope.$parent.location.latitude = event.latlng.lat;
        $scope.$parent.location.longitude = event.latlng.lng;
      });
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

    offsetBounds = function(bounds, offset) {
      return [
        [bounds[0][0] - offset, bounds[0][1] - offset],
        [bounds[1][0] + offset, bounds[1][1] + offset]
      ];
    };

    setMap = function(map) {
      ImageService.get(map.image).then(function(image) {
        leafletData.getMap().then(function(leafletMap) {
          var bounds = getImageBounds(image);
          leafletMap.eachLayer(function(layer) {
            leafletMap.removeLayer(layer);
          });
          new L.ImageOverlay( "/api/files/" + map.image, bounds).addTo(leafletMap).bringToFront();
          leafletMap.fitBounds(bounds);
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
      return $scope.$parent.location != null && $scope.$parent.location.mapRef != null;
    };

    // Listeners
    $scope.$parent.$watch("location.mapRef", function (newMapRef) {
      if ($scope.$parent.location !== null && $scope.$parent.location !== undefined) {
        $scope.$parent.location.latitude = null;
        $scope.$parent.location.longitude = null;
        setMap($scope.maps[newMapRef]);
      }
    });
	});

	app.directive("selectLocation", function() {
		return {
			restrict: "E",
			templateUrl: "/scripts/shared/directives/selectLocation/selectLocationView.html",
      transclude: true
		};
	});

}());
