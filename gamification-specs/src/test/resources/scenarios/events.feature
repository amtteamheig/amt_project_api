Feature: Event processing

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for the application "A1"
    Given there is a user "U1" with an ID for the application "A1"

  #Scenario: send an event
  #  Given I have an event payload
  #  When I POST the event payload to the /events endpoint
  #  Then The application receives a 200 status code
  #  When I send a GET to the user URL in the location header
  #  Then The application receives a 200 status code
  #  And I receive a payload with points and badges

  Scenario: get the list of users
    When I send a GET to the /users endpoint
    Then The application receives a 200 status code