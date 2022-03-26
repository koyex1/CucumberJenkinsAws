@snippet
Feature: Snippet

Scenario: Snippet

Given browser "chrome"
#When I visit "https://demo.imacros.net/Automate/Downloads" in window identified by "window1"
#And I download file from "(//a[text()='Download'])[1]"
When I download file from "https://filesamples.com/samples/document/txt/sample3.txt"
And I store its content in "downloadContent"
Then "downloadContent" should contain "Ciceronem"
Then download should be successful

#When I visit "https://www.dev2qa.com/demo/upload/uploadFileTest.html" in window identified by "window1"
#And I upload "https://filesamples.com/samples/document/txt/sample3.txt" to "//input[contains(@type, 'file')]"
#And I wait for 5 seconds
#And I click "//input[contains(@type, 'submit')]"
#Then "405 Not Allowed" page title should be shown

#Given variable "first" is "https://demo.applitools.com/"
#And browser "chrome"
#When I visit "«first»" in window identified by "window1"