package StepDefinitions;

import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;

import Utils.Algorithms;
import Utils.ChangingStepSpeech;
import Utils.ConvertToJson;
import Utils.PrintMessages;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.cucumber.java.AfterStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class DataBaseStepDefinition {

	Connection con ;
	public String speech="";
	String result;
	Algorithms customfunction = new Algorithms();
	

@Given("DB host {string} port {string} dbName {string} user {string} password {string}")
public void db_host(String host, String port , String dbName, String user, String password) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
	speech = ChangingStepSpeech.speechChange("Given");
	DTO.runningMessage = speech + " DB host \""+host+ "\" port \""+port+ "\" dbName \""+dbName+ "\" user \""+user+ "\" password \""+password+"\"";
	host = customfunction.extractionOfAngleBracketValues(host);
	port = customfunction.extractionOfAngleBracketValues(port);
	dbName = customfunction.extractionOfAngleBracketValues(dbName);
	user = customfunction.extractionOfAngleBracketValues(user);
	password = customfunction.extractionOfAngleBracketValues(password);
	DTO.runningDefinedMessage = speech + " DB host {"+host+ "} port {"+port+ "} dbName {"+dbName+ "} user {"+user+ "} password {"+password+"}";
	Class.forName("com.mysql.cj.jdbc.Driver"); 
	 con = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+dbName, user, password);
	
}


@When("I execute the query {string}")
public void i_execute_the_query(String queryStatement) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException{
	speech = ChangingStepSpeech.speechChange("When");
	DTO.runningMessage= speech + " I execute the query \""+ queryStatement+"\"";
	queryStatement = customfunction.extractionOfAngleBracketValues(queryStatement);
	DTO.runningDefinedMessage = speech + " I execute the query {"+ queryStatement+"}";
  	result = ConvertToJson.resultSetToJson(con, queryStatement);
  	System.out.println(result);
}

@When("I store the result in {string} as string")
public void i_store_the_result_in_as_string(String key) {
	speech = ChangingStepSpeech.speechChange("When");
	DTO.runningMessage= speech + " I store the result in \""+key+"\" as string";
	DTO.storeStrings.put(key, result);
	DTO.runningDefinedMessage= speech + " I store the result in {"+key+"} as string";
}

@When("I store the result in {string} as json")
public void i_store_the_result_in_as_json(String responseAsJson) throws ParseException {
	speech = ChangingStepSpeech.speechChange("When");
	DTO.runningMessage= speech + " I store the result in \""+responseAsJson+"\" as json";
	DTO.runningDefinedMessage= speech + " I store the result in {"+responseAsJson+"} as json";
	//JSONParser parser = new JSONParser();  
	JSONObject jsonResponseBody = new JSONObject();// parser.parse(result); 
	DTO.storeJsons.put(responseAsJson, jsonResponseBody);
	
}

}
