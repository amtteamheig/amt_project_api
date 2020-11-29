Feature: Basic operations on badges and pointScales

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid

  #
  # BADGES ENDPOINTS BASIC FEATURES
  #

  Scenario: get the list of badges when the list is empty
    Given I have a badge payload
    And I send a GET to the /badges endpoint
    Then I receive a 200 status code
    And I receive a list containing 0 badge(s)

  #
  # POINT SCALES ENDPOINTS BASIC FEATURES
  #

  Scenario: get the list of pointScale when the list is empty
    Given I have a pointScale payload
    When I send a GET to the /pointScales endpoint
    Then I receive a 200 status code
    And I receive a list containing 0 pointScale(s)
