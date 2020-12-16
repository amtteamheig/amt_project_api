Feature: Validation of badges implementation

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for the application "A1"

  #
  # Create a badge (POST / GET with entire payload check)
  #
  Scenario: a created badge can be retrieved with the proper data
    Given The application has a badge payload
    When The application "A1" POST the "Diamond" badge payload to the /badges endpoint
    Then The application receives a 201 status code
    When The application "A1" sends a GET to the badge URL in the location header
    Then The application receives a 200 status code
    And The application receives a payload that is the same as the badge payload

  #
  # Create a badge (POST) with bad attributes
  #
  Scenario: the API should return a 400 status code when the attributes are incorrect
    Given The application has a badge payload
    When The application "A1" POST the "" badge payload to the /badges endpoint
    Then The application receives a 400 status code
    Given The application has a badge payload
    And The application "A1" POST the "" date badge payload to the /badges endpoint
    Then The application receives a 400 status code

  #
  # Create a badge (POST) with imageURL is empty
  Scenario: the API should return a 201 status code when imageURL is empty
    Given The application has a badge payload
    When The application "A1" POST the "url" imageURL badge payload to the /badges endpoint
    Then The application receives a 201 status code

  #
  # Create a badge (POST / GET with user property check)
  #
  Scenario: 2 applications retrieve only their badges
    Given The application has a badge payload
    And there is a X-API-Key valid for the application "A2"
    When The application "A1" POST the "Golden" badge payload to the /badges endpoint
    And The application "A1" POST the "Silver" badge payload to the /badges endpoint
    And  The application "A2" POST the "Bronze" badge payload to the /badges endpoint
    Then The application "A1" GET to the /badges endpoint receive a list containing 2 badge(s)
    And The application "A2" GET to the /badges endpoint receive a list containing 1 badge(s)

  #
  # Check date of a badge (POST / GET with date check that it's the same day)
  #
  Scenario:
    Given The application has a badge payload
    When The application "A1" POST the "Diamond" badge payload to the /badges endpoint
    Then The application receives a 201 status code
    When The application "A1" sends a GET to the badge URL in the location header
    Then The application receives a 200 status code
    And The application receives a badge that was created today