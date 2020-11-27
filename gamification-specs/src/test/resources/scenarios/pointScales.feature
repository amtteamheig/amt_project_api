Feature: Check server is running

  Background:
    Given there is a Gamification server

  Scenario: get the list of pointScale
    Given I have a pointScale payload
    When I POST the pointScale payload to the /pointScales endpoint
    And I send a GET to the /pointScales endpoint
    Then I receive a 200 status code
    And I receive a list containing 1 pointScale(s)

  Scenario: get the list of pointScale when the list is empty
    Given I have a pointScale payload
    When I send a GET to the /pointScales endpoint
    Then I receive a 200 status code
    And I receive a list containing 0 pointScale(s)
