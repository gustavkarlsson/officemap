/* global angular */

(function () {
  "use strict";
  angular
    .module("main", [
      "ngAnimate",
      "ngAria",
      "ngCookies",
      "ngMessages",
      "ngResource",
      "ui.router",
      "ngSanitize",
      "ngTouch",
      "ngMaterial",
      "leaflet-directive",
      "angularFileUpload"
    ])
    .config(function ($locationProvider) {
      $locationProvider.html5Mode(true);
    })
    .config(function ($stateProvider, $urlRouterProvider) {
      $urlRouterProvider.otherwise("/");

      $stateProvider
        .state("home", {
          url: "/",
          templateUrl: "/scripts/components/home/homeView.html"
        })
        .state("maps", {
          url: "/maps/{ref:int}",
          templateUrl: "/scripts/components/map/mapView.html",
          controller: "MapController",
          resolve: {
            mapRef: function ($stateParams) {
              return $stateParams.ref;
            },
            map: function (MapService, $stateParams) {
              return MapService.get($stateParams.ref);
            },
            image: function (MapService, ImageService, $stateParams) {
              return MapService.get($stateParams.ref).then(function (map) {
                return ImageService.get(map.image);
              });
            },
            persons: function (PersonService, $stateParams) {
              return PersonService.getAllByMapRef($stateParams.ref);
            },
            activePersonRef: function () {
              return null;
            }
          }
        })
        .state("people", {
          url: "/people/{ref:int}",
          templateUrl: "/scripts/components/map/mapView.html",
          controller: "MapController",
          resolve: {
            mapRef: function (PersonService, $stateParams) {
              return PersonService.get($stateParams.ref).then(function (person) {
                return person.location.mapRef;
              });
            },
            map: function (PersonService, MapService, $stateParams) {
              return PersonService.get($stateParams.ref).then(function (person) {
                return MapService.get(person.location.mapRef);
              });
            },
            image: function (PersonService, MapService, ImageService, $stateParams) {
              return PersonService.get($stateParams.ref).then(function (person) {
                return MapService.get(person.location.mapRef).then(function (map) {
                  return ImageService.get(map.image);
                });
              });
            },
            persons: function (PersonService, $stateParams) {
              return PersonService.get($stateParams.ref).then(function (person) {
                return PersonService.getAllByMapRef(person.location.mapRef);
              });
            },
            activePersonRef: function ($stateParams) {
              return $stateParams.ref;
            }
          }
        })
        .state("admin", {
          url: "/admin",
          templateUrl: "/scripts/components/admin/adminView.html",
          controller: "AdminController",
          resolve: {
            persons: function (PersonService) {
              return PersonService.getAll();
            },
            maps: function (MapService) {
              return MapService.getAll();
            }
          }
        });

    });
}());
