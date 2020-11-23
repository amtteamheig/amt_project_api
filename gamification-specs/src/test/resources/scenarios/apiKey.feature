Feature: Check the API-Key

  Background:
    Given there is a Gamification server

  #
  # API-Key - No access
  #

  Scenario: try to access to badges without API-Key
    When I send a GET to the /badges endpoint
    Then I receive a 401 status code

  Scenario: try to access to pointScales without API-Key
    When I send a GET to the /pointScales endpoint
    Then I receive a 401 status code