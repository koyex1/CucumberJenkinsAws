Feature: Search Google invalid

Scenario: I should be able to search google 

Given browser "chrome"
When I visit "url"
And I fill "elementxpath" with value "Jesus"
And I press "Enter"
Then "elementxpath" should be shown