/* global alert */
/* global angular */
/* global L */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("SelectLocationController", function($scope, ImageService, leafletData) {
    // Variables
    var leafletMap,
      getImageBounds,
      offsetBounds,
      setMap;

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

    //Init
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

    // Listeners
    $scope.$watch("location.mapRef", function (newMapRef) {
      if ($scope.location !== null && $scope.location !== undefined) {
        $scope.location.latitude = null;
        $scope.location.longitude = null;
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
