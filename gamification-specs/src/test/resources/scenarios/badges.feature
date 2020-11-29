Feature: Validation of badges implementation

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for application "A1"

  #
  # Create a badge (POST / GET with entire payload check)
  #
  Scenario: a created badge can be retrieved with the proper data
    Given The application has a badge payload
    When The application "A1" POST the "Diamond" badge payload to the /badges endpoint
    Then The application receive a 201 status code
    When The application "A1" send a GET to the badge URL in the location header
    Then The application receive a 200 status code
    And The application receive a payload that is the same as the badge payload

  #
  # Create a badge (POST / GET with user property check)
  #
  Scenario: a created badge and only the created badge can be retrieved
    Given The application has a badge payload
    When The application "A1" POST the "Golden" badge payload to the /badges endpoint
    Then The application receive a 201 status code
    When The application "A1" send a GET to the /badges endpoint
    Then The application receive a 200 status code
    And The application "A1" receive a list containing 1 badge(s)

  #
  # Check date of a badge (POST / GET with date check that it's the same day)
  #
  Scenario:
    Given The application has a badge payload
    When The application "A1" POST the "Diamond" badge payload to the /badges endpoint
    Then The application receive a 201 status code
    When The application "A1" send a GET to the badge URL in the location header
    Then The application receive a 200 status code
    And The application receive a badge that was created today