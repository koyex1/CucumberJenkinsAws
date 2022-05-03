@ACME-1
Feature: ACME-1

Scenario: I should be able to login

Given browser "chrome"
And variable "URL" is "https://demo.applitools.com/"
And variable "delay" is "«generateRandomNumbers(10)»" 
When I visit "«URL»" in window identified by "window1"
And I wait for "4" seconds
And I click "«elements.signIn button»"
Then "«elements.search field»" should be shown
