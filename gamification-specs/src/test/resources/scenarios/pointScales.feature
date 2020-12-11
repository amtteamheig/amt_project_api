Feature: Check server is running

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for the application "A1"

  #
  # Create a point scale (POST / GET with entire payload check)
  #
  Scenario: a created point scale can be retrieved with the proper data
    Given The application has a pointScale payload
    When The application "A1" POST the "Diamond Rank" pointScale payload to the /pointScales endpoint
    Then The application receives a 201 status code
    When The application "A1" sends a GET to the pointScale URL in the location header
    Then The application receives a 200 status code
    And The application receives a payload that is the same as the pointScale payload

  #
  # Create a point scale (POST / GET with user property check)
  #
  Scenario: 2 applications retrieve only their point scales
    Given The application has a pointScale payload
    And there is a X-API-Key valid for the application "A2"
    When The application "A1" POST the "Golden Rank" pointScale payload to the /pointScales endpoint
    And The application "A1" POST the "Silver Rank" pointScale payload to the /pointScales endpoint
    And The application "A2" POST the "Bronze Rank" pointScale payload to the /pointScales endpoint
    Then The application "A1" GET to the /pointScales endpoint receive a list containing 2 pointScale(s)
    And The application "A2" GET to the /pointScales endpoint receive a list containing 1 pointScale(s)

  #
  # Create a point scale (POST) with bad attributes
  Scenario: the API should return a 400 status code when the attributes are incorrect
    Given The application has a pointScale payload
    When The application "A1" POST the "" pointScale payload to the /pointScales endpoint
    Then The application receives a 400 status code
    And The application "A1" POST the "" description pointScale payload to the /pointScales endpoint
    Then The application receives a 400 status code