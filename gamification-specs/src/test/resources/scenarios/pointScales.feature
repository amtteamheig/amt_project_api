Feature: Check server is running

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid

  #
  # Create a point scale (POST / GET with entire payload check)
  #
  Scenario: a created point scale can be retrieved with the proper data
    Given I have a pointScale payload
    When I POST the "Diamond Rank" pointScale payload to the /pointScales endpoint
    Then I receive a 201 status code
    When I send a GET to the pointScale URL in the location header
    Then I receive a 200 status code
    And I receive a payload that is the same as the pointScale payload

  #
  # Create a point scale (POST / GET with user property check)
  #
  Scenario: a created point scale can be retrieved with the proper data
    Given I have a pointScale payload
    When I POST the "Golden Rank" pointScale payload to the /pointScales endpoint
    Then I receive a 201 status code
    When I send a GET to the /pointScales endpoint
    Then I receive a 200 status code
    And I receive a list containing 1 pointScale(s)