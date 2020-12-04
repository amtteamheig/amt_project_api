# AMT - Gamification API Project

## Introduction
In this project we will create an API to enable any client to add a gamification value to their service. 

## Gamification Features

### Registration
Before you get to play with the gamification engine, you need to register your application into our database so we can distinguish which application is being used. Doing so will give you a key that will be required in the header of your requests for all endpoints.

#### Registration Endpoint

* [Register](docs/register.md) : `POST /registration`

### Users
To be able to distinguish statistics with more than one users we need to have users in our database. Those users will be automatically generated from events.

#### Users Endpoints

* [Get a specific user](docs/users_get_id.md) : `GET /users/{id}`
* [Get all users](docs/users_get.md) : `GET /users`

### Events
In order to link user events with our rules, we have the events endpoint.

*Example :*

*1) User liked a picture, tell the API this user liked a picture ! Does it validates any rules ? Yes. He liked 10 pictures so he gets the badge "Picture Lover" !*

#### Rules Endpoints

* [Indicate an event to the API](docs/events_post.md) : `POST /events`

### Create Rules
A rule will enable the application to determine how the user will be able to profit from the gamification engine.

*Example :*

*1) Facebook added a gamification service that gives you 1 point on every comment !*

#### Rules Endpoints

* [Create a rule](docs/rules_post.md) : `POST /rules`
* [Get a specific rule](docs/rules_get_id.md) : `GET /rules/{id}`
* [Get all rules](docs/rules_get.md) : `GET /rules`

### Create Point Scales
Point scales enables our clients to put a name on their gamification entities.

*Examples :*

*1) The StackOverflow Website created a new point scale "Reputation" to enable users to have a Reputation and get recognized within the StackOverflow community*

*2) A Library Website added the point scale "BookWorm" which enables users to see how many books they have read*

#### Point Scale Endpoints

* [Create a point scale](docs/pointScale_post.md) : `POST /pointScales`
* [Get a specific point scale](docs/pointScales_get_id.md) : `GET /pointScales/{id}`
* [Get all created point scales](docs/pointScale_get.md) : `GET /pointScales`

### Create Badges 

The client will be able to create badges. A badge can be rewarded to their users as a specific accomplishement defined by a rule.

*Example :*

*1) I upvoted 1'000 posts, I received the badge "Go Outside!"*

#### Badges Endpoints

* [Create a badge](docs/badges_post.md) : `POST /badges`
* [Get a specific badge](docs/badges_get_id.md) : `GET /badges/{id}`
* [Get all badges](docs/badges_get.md) : `GET /badges`
