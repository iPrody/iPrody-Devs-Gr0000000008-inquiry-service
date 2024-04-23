Feature: Find Inquiry by id

  Scenario:
    Given Inquiry endpoint "api/v1/inquiries/id/" with http method GET available
    When client wants to find a inquiry with id 2
    Then response code is 200