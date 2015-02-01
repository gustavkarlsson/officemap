# OfficeMap
Web application for managing persons and their positions on maps.
Meant to be used in big office environments to find people and get an overview of where people are located.

## Backend
Java backend built on [Dropwizard](http://dropwizard.io) to provide a RESTful API and
uses an embedded [H2](http://www.h2database.com/) database as default storage.

Follows the [Event Sourcing](http://martinfowler.com/eaaDev/EventSourcing.html) pattern
to store a complete change history of the internal state.

## Frontend
Built with [AngularJS](https://angularjs.org/) + [Bootstrap](http://getbootstrap.com) + [Leaflet](http://leafletjs.com/)

## Build
Run `mvn package` to build a full jar in the `target` directory.

## Run
First time: Run `java -jar officemap.jar db migrate` to initialize database.

Run `java -jar officemap.jar server` to start.
