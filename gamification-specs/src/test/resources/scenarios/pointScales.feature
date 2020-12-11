Feature: Validation of pointScale implementation

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

  Scenario: update point scale performs correctly
    Given The application has a pointScale payload
    When The application "A1" POST the "Platinum Rank" pointScale payload to the /pointScales endpoint
    Then The application receives a 201 status code
    When The application "A1" PATCH a point scale named "Platinum Rank", he want to change the attribute "name" with the value "Wood Rank"
    Then The application receives a 200 status code
    And The application "A1" contains a point scale named "Wood Rank" as "name" attribute


  Scenario: update point scale with a incorrect id
    When The application "A1" PATCH a point scale with the id 99
    Then The application receives a 404 status code

  Scenario: update a unknown attribute
    Given The application has a pointScale payload
    When The application "A1" POST the "Test Rank" pointScale payload to the /pointScales endpoint
    When The application "A1" PATCH a point scale named "Test Rank", he want to change the attribute "test" with the value "Bla"
    Then The application receives a 400 status code

