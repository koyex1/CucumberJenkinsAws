@MYSQLTESTDB-200
Feature: MYSQLTESTDB-200

Scenario: As a user, I should be able to search for all firstname, when I query the DB

Given DB host "localhost" port "3306" dbName "testdatabase" user "root" password "Passw@rd0123"
When I execute the query 'SELECT firstName FROM testdatabase.student'
Then I store the result in "result" as string
And I store the result in "result1" as json
And "result" should contain "Olumide"