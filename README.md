# AMT - Gamification API Project

## Introduction
In this project we will create an API to enable any client to add a gamification value to their service. 

## Gamification Features
A client will be able to : 

### Registration
Before you get to play with the gamification engine, you need to register your application into our database so we can distinguish which application is being used. Doing so will give you a key that will be required in the header of your requests for all endpoints.

#### Registration Endpoint

* [Register](docs/register.md) : `POST /registration`

### Create Rules
A rule will enable the application to determine how the user will be able to profit from the gamification engine.

*Example :*

*1) Facebook added a gamification service that gives you 1 point on every comment !*

#### Rules Endpoints

* [Create a rule](docs/pointScale.md) : `POST /pointScales`
* [Get all rules](docs/pointScale.md) : `GET /pointScales`


### Create Point Scales
Point scales enables our clients to put a name on their gamification entities.

*Examples :*

*1) The StackOverflow Website created a new point scale "Reputation" to enable users to have a Reputation and get recognized within the StackOverflow community*

*2) A Library Website added the point scale "BookWorm" which enables users to see how many books they have read*

#### Point Scale Endpoints

* [Create a point scale](docs/pointScale.md) : `POST /pointScales`
* [Get all created point scales](docs/pointScale.md) : `GET /pointScales`

### Create Badges 

The client will be able to create badges. A badge can be rewarded to their users.

* Create point scales = 
* Create rules