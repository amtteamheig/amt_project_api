Feature: Check the API-Key

  Background:
    Given there is a Gamification server

  #
  # API-Key - No access
  #

  Scenario: try to access to badges without API-Key
    When A unregistered application send a GET to the /badges endpoint
    Then The application receive a 401 status code

  Scenario: try to access to pointScales without API-Key
    When A unregistered application send a GET to the /pointScale endpoint
    Then The application receive a 401 status code