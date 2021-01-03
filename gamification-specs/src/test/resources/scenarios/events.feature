Feature: Event processing

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for the application "A1"
    Given there is a user "U1" with an ID for the application "A1"
    Given there is a X-API-Key valid for the application "A2"

  Scenario: the API should return a 400 status code when the attributes are incorrect
    Given The application has a "rule0" event payload
    When The application "A1" POST the event "" payload to the /events endpoint
    Then The application receives a 400 status code
    Given The application has a "rule0" event payload
    When The application "A1" POST the event timestamp "" payload to the /events endpoint
    Then The application receives a 400 status code
    Given The application has a "rule0" event payload
    When The application "A1" POST the event userId "" payload to the /events endpoint
    Then The application receives a 400 status code

  Scenario: send an event with no rule specified
    Given The application has a "" event payload
    When The application "A1" POST the event payload to the /events endpoint
    Then The application receives a 400 status code

  Scenario: send an event with a rule specified
    Given The application has a "rule1" event payload
    Given The application "A1" has a rule payload
    When The application "A1" POST the "rule1" rule payload to the /rules endpoint
    Then The application receives a 201 status code
    Given The application "A2" has a rule payload
    When The application "A2" POST the event payload to the /events endpoint
    Then The application receives a 400 status code
    When The application "A1" POST the event payload to the /events endpoint
    Then The application receives a 200 status code
    When The application sends a GET to the user URL in the location header
    Then The application receives a 200 status code
    And The application receives a payload with 1 point(s) and 1 badge(s)
    When The application "A1" POST the event payload to the /events endpoint
    Then The application receives a 200 status code
    When The application sends a GET to the user URL in the location header
    Then The application receives a 200 status code
    And The application receives a payload with 2 point(s) and 2 badge(s)
    When The application "A1" sends a GET to the /users endpoint
    Then The application receives a 200 status code
    And The application receives 1 users
    When The application "A2" sends a GET to the /users endpoint
    Then The application receives a 200 status code
    And The application receives 0 users