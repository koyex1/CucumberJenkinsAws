@CORRECTION-200
Feature: CORRECTION-200 

Scenario: Title of your scenario

#Given variable "first" is "https://demo.applitools.com/"
#Then "«first»" should match regex "p*"
 
Given DB host "«environment.Testing Env.DB-parameters.host»" port "«environment.Testing Env.DB-parameters.port»" dbName "«environment.Testing Env.DB-parameters.dbName»" user "«environment.Testing Env.DB-parameters.user»" password "«environment.Testing Env.DB-parameters.password»"
And variable "help" is "firstName"
When I execute the query 'SELECT «help» FROM testdatabase.student'
And I store the result in "result" as string
And I store the result in "result1" as json
And I put the jsonExpression "$[0].firstName" from "result1" into "firstAvailableFirstName"
Then "«result»" should contain "Olumide"


#Then "«»"



#Then I put jsonpath "" in "" as ""   need to be done for api and database
#Then "" should contain regex ""