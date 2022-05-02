@passE2e
Feature: passE2e

Scenario: I should be able to search google 

Given browser "chrome"
And variable "URL" is "https://demo.applitools.com/"
And variable "delay" is "«generateRandomNumbers(10)»" 
When I visit "«URL»" in window identified by "window1"
And I wait for "4" seconds
And I fill "«elements.username field»" with value "«variables.firstName»"
And I wait for "«delay»" seconds

#generateRandomNumbers(int num) generates a number from 1 to num