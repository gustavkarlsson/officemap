/* global angular */

(function() {
    "use strict";
    angular
        .module("main", [
            "ngAnimate",
            "ngAria",
            "ngCookies",
            "ngMessages",
            "ngResource",
            "ngRoute",
            "ngSanitize",
            "ngTouch",
            "ngMaterial",
            "leaflet-directive",
            "angularFileUpload"
        ])
        .config(function($locationProvider) {
            $locationProvider.html5Mode(true);
        })
        .config(function($routeProvider) {
            $routeProvider
                .when("/", {
                    templateUrl: "/scripts/components/home/homeView.html"
                })
                .when("/persons/:ref", {
                    templateUrl: "/scripts/components/map/mapView.html",
                    controller: "MapController",
                    resolve: {
                        mapRef: function(PersonService, $route) {
                            return PersonService.get($route.current.params.ref).then(function(person) {
                                return person.location.mapRef;
                            });
                        },
                        map: function(PersonService, MapService, $route) {
                            return PersonService.get($route.current.params.ref).then(function(person) {
                                return MapService.get(person.location.mapRef);
                            });
                        },
                        image: function(PersonService, MapService, ImageService, $route) {
                            return PersonService.get($route.current.params.ref).then(function(person) {
                                return MapService.get(person.location.mapRef).then(function(map) {
                                    return ImageService.get(map.image);
                                });
                            });
                        },
                        persons: function(PersonService, $route) {
                            return PersonService.get($route.current.params.ref).then(function(person) {
                                return PersonService.getAllByMapRef(person.location.mapRef);
                            });
                        },
                        activePersonRef: function($route) {
                            return $route.current.params.ref;
                        }
                    }
                })
                .when("/maps/:ref", {
                    templateUrl: "/scripts/components/map/mapView.html",
                    controller: "MapController",
                    resolve: {
                        mapRef: function($route) {
                            return $route.current.params.ref;
                        },
                        map: function(MapService, $route) {
                            return MapService.get($route.current.params.ref);
                        },
                        image: function(MapService, ImageService, $route) {
                            return MapService.get($route.current.params.ref).then(function(map) {
                                return ImageService.get(map.image);
                            });
                        },
                        persons: function(PersonService, $route) {
                            return PersonService.getAllByMapRef($route.current.params.ref);
                        },
                        activePersonRef: function() {
                            return null;
                        }
                    }
                })
                .when("/admin", {
                    templateUrl: "/scripts/components/admin/adminView.html",
                    controller: "AdminController",
                    resolve: {
                        persons: function(PersonService) {
                            return PersonService.getAll();
                        },
                        maps: function(MapService) {
                            return MapService.getAll();
                        }
                    }
                })
                .when("/admin/maps/new", {
                    templateUrl: "/scripts/components/editMap/editMapView.html",
                    controller: "EditMapController",
                    resolve: {
                        ref: function($route) {
                            return null;
                        },
                        map: function(MapService, $route) {
                            return null;
                        }
                    }
                })
                .when("/admin/maps/:ref", {
                    templateUrl: "/scripts/components/editMap/editMapView.html",
                    controller: "EditMapController",
                    resolve: {
                        ref: function($route) {
                            return $route.current.params.ref;
                        },
                        map: function(MapService, $route) {
                            return MapService.get($route.current.params.ref);
                        }
                    }
                })
                .when("/admin/persons/new", {
                    templateUrl: "/scripts/components/editPerson/editPersonView.html",
                    controller: "EditPersonController",
                    resolve: {
                        ref: function($route) {
                            return null;
                        },
                        person: function(PersonService, $route) {
                            return null;
                        }
                    }
                })
                .when("/admin/persons/:ref", {
                    templateUrl: "/scripts/components/editPerson/editPersonView.html",
                    controller: "EditPersonController",
                    resolve: {
                        ref: function($route) {
                            return $route.current.params.ref;
                        },
                        person: function(PersonService, $route) {
                            return PersonService.get($route.current.params.ref);
                        }
                    }
                })
                .otherwise({
                    redirectTo: "/"
                });
        });

}());