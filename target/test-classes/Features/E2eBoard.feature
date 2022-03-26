@E2eBoard
Feature: Search Google

Scenario: I should be able to search google 

Given browser "chrome"
When I visit "https://demo.applitools.com/" in window identified by "window1"
And I wait for 4 seconds
And I fill "//input[@id='username']" with value "testUser"
And I fill "//input[@id='password']" with value "testUser"
And I click "//a[@id='log-in']" 
Then "//div[contains(@class, 'logged-user-w')][1]" should be shown
And "ACME" page title should be shown
And "https://demo.applitools.com/app.html" url should be shown

When I count "//div[contains(@class, 'logged-user-w')]" as "numberOfAvatars"
Then "numberOfAvatars" should equals 2

#Record element attribute value ie class, id, name values
When I record attribute "class" value in "//div[contains(@class, 'logged-user-w')][1]" as "className"
Then "className" should equals "logged-user-w"

When I visit "https://demo.applitools.com/" in window identified by "window2"
And I wait for 4 seconds
Then "//input[@id='username']" should be shown

When I close window "window2"
And I switch to window "window1"
Then "ACME" page title should be shown

When I visit "https://demoqa.com/automation-practice-form" in window identified by "window1"
And I scroll to "//div//input[@value='Male']/following-sibling::label"
And I click "//div//input[@value='Male']/following-sibling::label"
And I scroll to "//label[ contains(text(), 'State and City')]"
And I "check" checkbox "(//input[@type='checkbox'])[1]/following-sibling::label"
And I wait for 2 seconds
And I "check" checkbox "(//input[@type='checkbox'])[2]/following-sibling::label"
And I wait for 2 seconds
And I "uncheck" checkbox "(//input[@type='checkbox'])[1]/following-sibling::label"
And I wait for 2 seconds
And I "uncheck" checkbox "(//input[@type='checkbox'])[2]/following-sibling::label"
And I wait for 2 seconds
And I visit "https://computer-database.gatling.io/computers" in window identified by "window1"
And I click on "//a[contains(text(), 'Next')]" every 3 seconds until "//a[contains(text(), 'Apple Lisa')]" is shown
Then "//a[contains(text(), 'Next')]" every 3 seconds until "//a[contains(text(), 'Apple Lisa')]" should be shown

When I visit "https://sortablejs.github.io/Sortable" in window identified by "window1"
And I scroll to "//div[@id='shared-lists']//div[@id='example2-left']//div[contains(text(), 'Item 5')]"
And I drag and drop "//div[@id='shared-lists']//div[@id='example2-left']//div[contains(text(), 'Item 1')]" to "//div[@id='shared-lists']//div[@id='example2-right']"
And I wait for 3 seconds
And I drag and drop "//div[@id='shared-lists']//div[@id='example2-left']//div[contains(text(), 'Item 2')]" to "//div[@id='shared-lists']//div[@id='example2-right']"
And I wait for 3 seconds
When I scroll to "//div[@id='thresholds']//input[@type='range']"
And I set slide "//div[@id='thresholds']//input[@type='range']" to axis -50 and 0

When I download file from "https://filesamples.com/samples/document/txt/sample3.txt"
And I store its content in "downloadContent"
Then "downloadContent" should contain "Ciceronem"
Then download should be successful

When I visit "https://demo.imacros.net/Automate/Downloads" in window identified by "window1"
And I download file from "(//a[text()='Download'])[1]"
Then download should be successful

When I visit "https://www.dev2qa.com/demo/upload/uploadFileTest.html" in window identified by "window1"
And I upload "https://filesamples.com/samples/document/txt/sample3.txt" to "//input[contains(@type, 'file')]"
And I wait for 5 seconds
And I click "//input[contains(@type, 'submit')]"
Then "405 Not Allowed" page title should be shown

Given variable "first" is "https://demo.applitools.com/"
And browser "chrome"
When I visit "«first»" in window identified by "window1"
Then "«first»" url should be shown
