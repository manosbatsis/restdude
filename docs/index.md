
# Overview

Restdude uses your models to provide simple and flexible scaffolding for rapid application development. It also keeps your code base at a minimum and encourages component-based development to improve quality and maintainability.

## Quick Build and Run

Follow the steps bellow to build.

a) Clone the repository:

    git clone https://github.com/manosbatsis/restdude.git

b) Copy HOWTO.txt to dev.properties:

    cp HOWTO.txt dev.properties

c) Build and install ( -U forces the download of updated snapshot dependencies):

    mvn -U clean install

d) run embedded Jetty container:

    cd restdude-war-overlay
    mvn jetty:run


e) Browse the app client: http://localhost:8080/calipso/client or the API reference: http://localhost:8080/calipso/apidoc. See also the Swagger API Reference section bellow.

If you are using chrome, ensure you have enabled cookies for localhost.

## Optimize Profile

If have [node](https://nodejs.org) installed, you can use the optimize profile and jetty:run-war to optimize client-side code:

    cd restdude-war-overlay
    mvn -P optimize clean jetty:run-war


## Integration Testing Profile

To run integration tests simply activate the ci profile:

    mvn clean install -P ci


