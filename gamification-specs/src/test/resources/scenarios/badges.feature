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
    When I send a GET to the badge URL in the location header
    Then I receive a 200 status code
    And I receive a payload that is the same as the badge payload

  #
  # Create a badge (POST / GET with user property check)
  #
  Scenario: a created badge and only the created badge can be retrieved
    Given I have a badge payload
    When I POST the "Golden" badge payload to the /badges endpoint
    Then I receive a 201 status code
    When I send a GET to the /badges endpoint
    Then I receive a 200 status code
    And I receive a list containing 1 badge(s)

  #
  # Check date of a badge (POST / GET with date check that it's the same day)
  #
  Scenario:
    Given I have a badge payload
    When I POST the "Diamond" badge payload to the /badges endpoint
    Then I receive a 201 status code
    When I send a GET to the badge URL in the location header
    Then I receive a 200 status code
    And I receive a badge that was created today

  Scenario: update badge
    Given I have a badge payload
    When I POST the "Diamond" badge payload to the /badges endpoint
    Then I receive a 201 status code
      @badge =
    When I PATCH the last send badge and change the name with "Golden"
    Then I receive a 200 status code

