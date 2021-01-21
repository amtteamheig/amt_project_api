# AMT - Gamification API Project

## Introduction
In this project we will create an API to enable any client to add a gamification value to their service. 

## Build and run our API

You can use maven to build and run the REST API implementation from the command line. After invoking the following maven goal, the Spring Boot server will be up and running, listening for connections on port 8080.

```
cd gamification-impl/
mvn spring-boot:run
```

You can then access:

* the [API documentation](http://localhost:8080/swagger-ui.html), generated from annotations in the code
* the [API endpoint](http://localhost:8080/), accepting GET and POST requests

## Populating the api

Once the server is running, you can populate it with `./populate-api.sh`. This will create :
- 3 badges : Gold, Silver and Bronze
- 2 pointScales : Activity and Curiosity
- 4 Rules (thus 4 type of events) : 
    - "firstPost" : gives you a gold badge and 3 activity points
    - "questionPosted" : gives you a silver badge and 3 activity points
    - "commentPosted" : gives you a silver badge and 2 activity points
    - "openAQuestion" : gives you a bronze badge and 1 curiosity point

If you want to also create 3 test users, run `./populate-api.sh -u`.

In both cases, you will recieve an API key that you can then use to access your data.

We had trouble making the script work on some MACOS versions, but it works fine on Linux and Windows.

## Cucumber testing

We have tested a lot of things, those include:
- The apiKey working as intended. In most of the features also tested, we verify that when 2 applications use the feature, they retreive only their data
- The badges and pointScales features, but also their updates and the checks on the fields. For instance, if a important field is missing when creating a badge, the server should send a 4XX status code
- The rules features, creation and checks
- The events, checks if the users are created corretely and the data can be retrieved

You can find all of those tests in the `gamification-specs` folder. Simply run a `mvn clean install` followed by a run of the class ``SpecificationTest``

