/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("LeafletController", function ($scope, MapService, ImageService, leafletData, ref, map) {
		
		// Variable declarations
		var getLeafletMap, getImageBounds, offsetBounds;
		
		// Init
		MapService.setActive(ref);
		getLeafletMap = leafletData.getMap;
		
		// Methods
		getImageBounds = function (img) {
			return [[-(img.height / 2), -(img.width / 2)], [img.height / 2, img.width / 2]];
		};
		
		offsetBounds = function (bounds, offset) {
			return [[bounds[0][0] - offset, bounds[0][1] - offset], [bounds[1][0] + offset, bounds[1][1] + offset]];
		};
		
		// Build Leaflet Map
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
					dummy: {
						name: "dummy",
						type: "imageOverlay",
						url: "images/dummy_map.png",
						bounds: [[0, 0], [0, 0]],
						layerParams: {
							noWrap: true
						}
					}
				}
			}
		});
		
		// Clear layers
		getLeafletMap().then(function (leafletMap) {
			leafletMap.eachLayer(function (layer) {
				leafletMap.removeLayer(layer);
			});
		});
		
		// Get dimensions of map and add map layer
		ImageService.load("api/files/" + map.image).then(
			function (img) {
				getLeafletMap().then(function (leafletMap) {
					var bounds, maxBounds;
					bounds = getImageBounds(img);
					maxBounds = offsetBounds(bounds, 200);
					L.imageOverlay("api/files/" + map.image, bounds).addTo(leafletMap).bringToFront();
					leafletMap.setMaxBounds(maxBounds);
				});
			},
			function (reason) {
				alert("Failed: " + reason);
			}
		);
	});

}());