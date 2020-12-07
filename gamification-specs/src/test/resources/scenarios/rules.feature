Feature: Validation of rules implementation

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for the application "A1"
    Given there is a X-API-Key valid for the application "A2"

  #
  # Create a rule (POST / GET with entire payload check)
  #
  Scenario: a created rule can be retrieved with the proper data
    Given The application has a rule payload
    When The application "A1" POST the "rule1" rule payload to the /rules endpoint
    Then The application receives a 201 status code
    When The application "A1" sends a GET to the rule URL in the location header
    Then The application receives a 200 status code
    And The application receives a payload that is the same as the rule payload

  #
  # Create a rule (POST / GET with user property check)
  #
  Scenario: 2 applications retrieve only their rules
    Given The application has a rule payload
    When The application "A1" POST the "rule1" rule payload to the /rules endpoint
    And The application "A1" POST the "rule2" rule payload to the /rules endpoint
    And  The application "A2" POST the "rule3" rule payload to the /rules endpoint
    Then The application "A1" GET to the /rules endpoint receive a list containing 2 rule(s)
    And The application "A2" GET to the /rules endpoint receive a list containing 1 rule(s)

  Scenario: check duplicates : the same app cannot define 2 times the same rule
    Given The application has a rule payload
    When The application "A1" POST the "rule4" rule payload to the /rules endpoint
    Then The application receives a 201 status code
    When The application "A1" POST the "rule4" rule payload to the /rules endpoint
    Then The application receives a 409 status code
    When The application "A2" POST the "rule4" rule payload to the /rules endpoint
    Then The application receives a 201 status code