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

