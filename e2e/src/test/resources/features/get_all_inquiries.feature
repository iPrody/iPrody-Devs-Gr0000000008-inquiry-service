Feature: Get all Inquiries

  Scenario:
    Given Inquiries endpoint "api/v1/inquiries/" with http method GET available
    When client wants to get first page
    Then response code is 200