/* global L */
/* global angular */

(function() {
	"use strict";
	var app = angular.module("main");

	app.controller("MapController", function($scope, MapService, ImageService, leafletData, mapRef, map, image, persons, activePersonRef) {
		// Variables
		var getImageBounds,
			offsetBounds,
			createMarkers;

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

    createMarkers = function() {
      var personRef,
        person,
        markers = [];
      for (personRef in persons) {
        if (persons.hasOwnProperty(personRef)) {
          personRef = parseInt(personRef);
          person = persons[personRef];
          markers.push({
            lat: person.location.latitude,
            lng: person.location.longitude,
            message: '<a ui-sref="people({ref: ' + personRef + '})">' + person.firstName + ' ' + person.lastName + '</a>',
            focus: personRef === activePersonRef,
            draggable: false
          });
        }
      }
      return markers;
    };

    //Scope
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
            maxBounds: offsetBounds(getImageBounds(image), 200)
          }
        }
      },
      markers: createMarkers()
    });
	});

}());
