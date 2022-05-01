@failE2e
Feature: failE2e

Scenario: I should be able to search google 

Given browser "chrome"
When I visit "https://demo.applitools.com/" in window identified by "window1"
And I wait for "4" seconds
And I fill "//input[@id='usernae']" with value "testUser"