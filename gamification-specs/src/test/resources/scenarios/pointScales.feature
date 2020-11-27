Feature: Check server is running

  Background:
    Given there is a Gamification server

  #
  # Create a point scale (POST / GET with entire payload check)
  #
  Scenario: a created badge can be retrieved with the proper data
    Given I have a pointScale payload
    When I POST the "Diamond Rank" pointScale of value 100 payload to the /pointScales endpoint
    Then I receive a 201 status code
    When I send a GET to the URL in the location header for pointScales
    Then I receive a 200 status code
    And I receive a payload that is the same as the pointScale payload
