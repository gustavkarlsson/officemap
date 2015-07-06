/* global L */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("MapController", function($scope, MapService, ImageService, leafletData, mapRef, map, image, persons,
		activePersonRef) {

		// Variable declarations
		var getLeafletMap,
			getImageBounds,
			offsetBounds,
			createMarkers;

		// Init
		getLeafletMap = leafletData.getMap;

		// Methods
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

		createMarkers = function() {
			var personRef,
				person,
				markers = [];
			for (personRef in persons) {
				person = persons[personRef];
				markers.push({
					lat: person.location.latitude,
					lng: person.location.longitude,
					message: '<a href="/persons/' + personRef + '">' + person.firstName + ' ' + person.lastName + '</a>',
					focus: personRef == activePersonRef,
					draggable: false
				});
			}
			return markers;
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
			},
			markers: createMarkers()
		});
	});

}());
