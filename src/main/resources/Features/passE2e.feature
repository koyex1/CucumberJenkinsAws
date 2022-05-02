@passE2e
Feature: passE2e

Scenario: I should be able to search google 

Given browser "chrome"
And variable "URL" is "https://demo.applitools.com/"
When I visit "«URL»" in window identified by "window1"
And I wait for "4" seconds
And I fill "«elements.username field»" with value "«variables.firstName»"
And I wait for "4" seconds