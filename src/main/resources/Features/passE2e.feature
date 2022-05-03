@passE2e
Feature: passE2e

Scenario: I should be able to search google 

Given browser "chrome"
And variable "URL" is "https://demo.applitools.com/"
And variable "delay" is "«generateRandomNumbers(10)»" 
When I visit "«URL»" in window identified by "window1"
And I wait for "4" seconds
And I fill "«elements.username field»" with value "«variables.firstName» and a randomName «generateRandomStrings(5)»"
And I wait for "«delay»" seconds
And "ACME" page title should be shown

When I download file from "https://filesamples.com/samples/document/txt/sample3.txt"
And I store its content in "downloadContent"
Then download should be successful
And "«downloadContent»" should contain "Ciceronem"

Given DB host "«environment.Testing Env.DB-parameters.host»" port "«environment.Testing Env.DB-parameters.port»" dbName "«environment.Testing Env.DB-parameters.dbName»" user "«environment.Testing Env.DB-parameters.user»" password "«environment.Testing Env.DB-parameters.password»"
When I execute the query 'SELECT firstName FROM testdatabase.student'
Then I store the result in "result" as string
And "«result»" should contain "Olumide"

#generateRandomNumbers(int num) generates a number from 1 to num
