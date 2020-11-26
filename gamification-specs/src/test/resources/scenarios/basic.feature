Feature: Basic operations on badges

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid

  #
  # BADGES BASIC FEATURES
  #

  Scenario: create a badge
    Given I have a badge payload
    When I POST the badge payload to the /badges endpoint
    Then I receive a 201 status code
    When I send a GET to the badge URL in the location header
    Then I receive a 200 status code
    And I receive a payload that is the same as the badge payload

  Scenario: get the list of badges
    When I send a GET to the /badges endpoint
    Then I receive a 200 status code

  #
  # POINT SCALES BASIC FEATURES
  #

  Scenario: create a pointScale
    Given I have a pointScale payload
    When I POST the pointScale payload to the /pointScales endpoint
    Then I receive a 201 status code
    When I send a GET to the pointScale URL in the location header
    Then I receive a 200 status code
    And I receive a payload that is the same as the pointScale payload

  Scenario: get the list of pointScale
    When I send a GET to the /pointScales endpoint
    Then I receive a 200 status code
