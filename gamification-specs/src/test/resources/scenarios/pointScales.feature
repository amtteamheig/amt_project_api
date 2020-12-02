Feature: Check server is running

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for application "A1"

  #
  # Create a point scale (POST / GET with entire payload check)
  #
  Scenario: a created point scale can be retrieved with the proper data
    Given The application have a pointScale payload
    When The application "A1" POST the "Diamond Rank" pointScale payload to the /pointScales endpoint
    Then The application receive a 201 status code
    When The application "A1" send a GET to the pointScale URL in the location header
    Then The application receive a 200 status code
    And The application receive a payload that is the same as the pointScale payload

  #
  # Create a point scale (POST / GET with user property check)
  #
  Scenario: 2 applications retrieve only their point scales
    Given The application have a pointScale payload
    And there is a X-API-Key valid for application "A2"
    When The application "A1" POST the "Golden Rank" pointScale payload to the /pointScales endpoint
    And The application "A1" POST the "Silver Rank" pointScale payload to the /pointScales endpoint
    And The application "A2" POST the "Bronze Rank" pointScale payload to the /pointScales endpoint
    Then The application "A1" GET to the /pointScales endpoint receive a list containing 2 pointScale(s)
    And The application "A2" GET to the /pointScales endpoint receive a list containing 1 pointScale(s)