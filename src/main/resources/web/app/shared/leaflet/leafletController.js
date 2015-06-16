/*global alert */
/*global L */
/*global angular */

(function () {
	"use strict";
	var app = angular.module("main");

	app.controller("LeafletController", function ($scope, MapService, ImageService, leafletData, mapRef, map, image, persons, activePerson) {

		// Variable declarations
		var getLeafletMap,
            getImageBounds,
            offsetBounds;

		// Init
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
            mapRef: mapRef,
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
						url: "/api/files/" + map.image,
						bounds: getImageBounds(image),
						maxBounds: offsetBounds(getImageBounds(image), 200),
						layerParams: {
							noWrap: true
						}
					}
				}
			}
		});
	});

}());