Feature: Validation of badges implementation

  Background:
    Given there is a Gamification server
    Given there is a X-API-Key valid

  #
  # Create a badge (POST / GET with entire payload check)
  #
  Scenario: a created badge can be retrieved with the proper data
    Given I have a badge payload
    When I POST the "Diamond" badge payload to the /badges endpoint
    Then I receive a 201 status code
    When I send a GET to the URL in the location header for badges
    Then I receive a 200 status code
    And I receive a payload that is the same as the badge payload

  #
  # Check date of a badge (POST / GET with date check that it's the same day)
  #
  Scenario:
    Given I have a badge payload
    When I POST the "Diamond" badge payload to the /badges endpoint
    Then I receive a 201 status code
    When I send a GET to the URL in the location header for badges
    Then I receive a 200 status code
    And I receive a badge that was created today