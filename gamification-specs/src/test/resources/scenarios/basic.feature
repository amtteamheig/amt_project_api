Feature: Basic operations on badges

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid

  #
  # BADGES ENDPOINTS BASIC FEATURES
  #

  Scenario: create a badge
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code

  Scenario: get the list of badges
    Given I have a badge payload
    And I send a GET to the /badges endpoint
    Then I receive a 200 status code

  #
  # POINT SCALES ENDPOINTS BASIC FEATURES
  #

  Scenario: create a pointScale
    Given I have a pointScale payload
    When I POST the pointScale payload to the /pointScales endpoint
    Then I receive a 201 status code

  Scenario: get the list of pointScale
    Given I have a pointScale payload
    When I POST the pointScale payload to the /pointScales endpoint
    And I send a GET to the /pointScales endpoint
    Then I receive a 200 status code
