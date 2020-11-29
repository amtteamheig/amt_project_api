Feature: Basic operations on badges and pointScales

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid for application "A1"

  #
  # BADGES ENDPOINTS BASIC FEATURES
  #

  Scenario: get the list of badges when the list is empty
    Given The application has a badge payload
    And The application "A1" send a GET to the /badges endpoint
    Then The application receive a 200 status code
    And The application "A1" receive a list containing 0 badge(s)

  #
  # POINT SCALES ENDPOINTS BASIC FEATURES
  #

  Scenario: get the list of pointScale when the list is empty
    Given The application have a pointScale payload
    When The application "A1" send a GET to the /pointScales endpoint
    Then The application receive a 200 status code
    And The application "A1" receive a list containing 0 pointScale(s)
