#<<>> and "" are all string values 
#everything are strings expect json stored values - how to not get confused about this wil be explained
#meaning json is the only one that doesn't use <<>> just mentions its variable straight in the double quotes "";
#because it felt totally unneccessary to scan for json as a stored local variable in my algorithm
#given browser - without anything default is chrome
#given browser "chrome"
#when I vist a url in window indenfied by "windowName" - from that point on windowName is runs all the other steps


@E2EBoard
Feature: E2EBoard

Scenario: I should be able to search google 

Given browser "chrome"
When I visit "https://demo.applitools.com/" in window identified by "window1"
And I wait for "4" seconds
And I fill "//input[@id='usernae']" with value "testUser"
And I fill "//input[@id='password']" with value "testUser"
And I click "//a[@id='log-in']" 
Then "//div[contains(@class, 'logged-user-w')][1]" should be shown
And "ACME" page title should be shown
And "https://demo.applitools.com/app.html" url should be shown

When I count "//div[contains(@class, 'logged-user-w')]" as "numberOfAvatars"
Then "numberOfAvatars" should equals "2"

#Record element attribute value ie class, id, name values
When I record attribute "class" value in "//div[contains(@class, 'logged-user-w')][1]" as "className"
Then "className" should equals "logged-user-w"

When I visit "https://demo.applitools.com/" in window identified by "window2"
And I wait for "4" seconds
Then "//input[@id='username']" should be shown

When I close window "window2"
And I switch to window "window1"
Then "ACME" page title should be shown

When I visit "https://demoqa.com/automation-practice-form" in window identified by "window1"
And I scroll to "//div//input[@value='Male']/following-sibling::label"
And I click "//div//input[@value='Male']/following-sibling::label"
And I scroll to "//label[ contains(text(), 'State and City')]"
And I "check" checkbox "(//input[@type='checkbox'])[1]/following-sibling::label"
And I wait for "2" seconds
And I "check" checkbox "(//input[@type='checkbox'])[2]/following-sibling::label"
And I wait for "2" seconds
And I "uncheck" checkbox "(//input[@type='checkbox'])[1]/following-sibling::label"
And I wait for "2" seconds
And I "uncheck" checkbox "(//input[@type='checkbox'])[2]/following-sibling::label"
And I wait for "2" seconds
And I visit "https://computer-database.gatling.io/computers" in window identified by "window1"
And I click on "//a[contains(text(), 'Next')]" every "3" seconds until "//a[contains(text(), 'Apple Lisa')]" is shown
Then "//a[contains(text(), 'Apple Lisa')]" should be shown

When I visit "https://sortablejs.github.io/Sortable" in window identified by "window1"
And I scroll to "//div[@id='shared-lists']//div[@id='example2-left']//div[contains(text(), 'Item 5')]"
And I drag and drop "//div[@id='shared-lists']//div[@id='example2-left']//div[contains(text(), 'Item 1')]" to "//div[@id='shared-lists']//div[@id='example2-right']"
And I wait for "3" seconds
And I drag and drop "//div[@id='shared-lists']//div[@id='example2-left']//div[contains(text(), 'Item 2')]" to "//div[@id='shared-lists']//div[@id='example2-right']"
And I wait for "3" seconds
When I scroll to "//div[@id='thresholds']//input[@type='range']"
And I set slide "//div[@id='thresholds']//input[@type='range']" to axis "-50" and "0"

When I download file from "https://filesamples.com/samples/document/txt/sample3.txt"
And I store its content in "downloadContent"
Then "downloadContent" should contain "Ciceronem"
Then download should be successful

When I visit "https://demo.imacros.net/Automate/Downloads" in window identified by "window1"
And I download file from "(//a[text()='Download'])[1]"
Then download should be successful

When I visit "https://www.dev2qa.com/demo/upload/uploadFileTest.html" in window identified by "window1"
And I upload "https://filesamples.com/samples/document/txt/sample3.txt" to "//input[contains(@type, 'file')]"
And I wait for "5" seconds
And I click "//input[contains(@type, 'submit')]"
Then "405 Not Allowed" page title should be shown

Given variable "first" is "https://demo.applitools.com/"
And browser "chrome"
When I visit "«first»" in window identified by "window1"
Then "«first»" url should be shown

When I download file from "https://filesamples.com/samples/document/txt/sample3.txt"
And I store its content in "downloadContent"
Then "downloadContent" should contain "Ciceronem"
Then download should be successful

When I visit "https://demo.imacros.net/Automate/Downloads" in window identified by "window1"
And I download file from "(//a[text()='Download'])[1]"

Given API Request
When I set "Authorization" header to "Bearer a6482d4cb30e16580a4924ed83aa44f0aca15680190ffbaf76f15bb7a5268997"
And I set "Content-Type" header to "application/json"
And I set "Accept" header to "application/json"
And I set body to '{"name":"Olumide Koyenikan", "gender":"male", "email":"olbiokffmn.pe@15ce.com", "status":"active"}'
And I execute POST for "https://gorest.co.in/public/v2/users"
Then response status should be "201"
And I store the response in "responseResult2" as json
And I store the response in "responseResult" as string
And "responseResult" should contain "Olumide"

Given DB host "localhost" port "3306" dbName "testdatabase" user "root" password "Passw@rd0123"
When I execute the query 'SELECT firstName FROM testdatabase.student'
Then I store the result in "result" as string
And I store the result in "result1" as json
And "result" should contain "Olumide"
And I put the jsonExpression "$.id" from "result1" into "firstAvailableFirstName"