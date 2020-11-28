Feature: Event processing

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid
    Given there is a user with an ID

  Scenario: send an event
    Given I have an event payload
    When I POST the event payload to the /events endpoint
    Then I receive a 200 status code
    When I send a GET to the user URL in the location header
    Then I receive a 200 status code
    And I receive a payload with points and badges

  Scenario: get the list of users
    When I send a GET to the /users endpoint
    Then I receive a 200 status code