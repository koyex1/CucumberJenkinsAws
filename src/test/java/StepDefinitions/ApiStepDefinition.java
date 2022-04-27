package StepDefinitions;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.filter.ValueNodes.JsonNode;

import Utils.Algorithms;
import Utils.ChangingStepSpeech;
import Utils.PrintMessages;
import io.cucumber.java.AfterStep;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiStepDefinition {

	RestAssured restA = new RestAssured();
	RequestSpecification InitRequest = RestAssured.given().urlEncodingEnabled(false);
	Response response;
	String strResponseBody;
	String statusCode;
	BrowserStepDefinitions store = new BrowserStepDefinitions();
	JSONObject requestParam = new JSONObject();
	String runningMessage;
	String speech = "";
	Algorithms customfunction = new Algorithms();
	
	@Test
	@Given("API Request")
	public void api_request(){
		speech = ChangingStepSpeech.speechChange("Given");
		DTO.runningMessage = speech + " API Request";
		DTO.runningDefinedMessage = speech + " API Request";
		
		 
	}
	
	
	@Test
	@When("I set {string} header to {string}")
	public void i_set_header_to(String key, String value) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException{
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I set \"" + key + "\" header to " +value+"\"";
		key = customfunction.extractionOfAngleBracketValues(key);
		value = customfunction.extractionOfAngleBracketValues(value);
		DTO.runningDefinedMessage = speech + " I set {" + key + "} header to {" +value+"}";
		InitRequest.header(key,value);
		

	}
	
	@Test
	@When("I set body to {string}")
	public void i_set_body_to(String bodyParam) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I set body to \"" + bodyParam+"\"";
		bodyParam = customfunction.extractionOfAngleBracketValues(bodyParam);
		DTO.runningDefinedMessage = speech + " I set body to {" + bodyParam+"}";
		System.out.println(bodyParam);
	    InitRequest.body(bodyParam);
	   
	}
	
	@Test
	@When("I execute POST for {string}")
	public void i_execute_post_for(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I execute POST for \"" + url+"\"";
		url = customfunction.extractionOfAngleBracketValues(url);
		DTO.runningDefinedMessage = speech + " I execute POST for {" + url+"}";
	    restA.baseURI = url;
	    response = InitRequest.post(url);
	    strResponseBody = response.getBody().asString();
	    System.out.println(strResponseBody);
	    
	}
	
	@Test
	@When("I execute GET for {string}")
	public void i_execute_get_for(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I execute GET for \"" + url+"\"";
		url = customfunction.extractionOfAngleBracketValues(url);
		DTO.runningDefinedMessage = speech + " I execute GET for {" + url+"}";
		restA.baseURI = url;
	    response = InitRequest.get(url);
	    strResponseBody = response.getBody().asString();
	    System.out.println(strResponseBody);
	    
	}
	
	@Test
	@When("I execute PUT for {string}")
	public void i_execute_put_for(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I execute PUT for \"" + url+"\"";
		url = customfunction.extractionOfAngleBracketValues(url);
		DTO.runningDefinedMessage = speech + " I execute PUT for {" + url+"}";
		restA.baseURI = url;
	    response = InitRequest.put(url);
	    strResponseBody = response.getBody().asString();
	    System.out.println(strResponseBody);
	    
	}
	
	@Test
	@When("I execute DELETE for {string}")
	public void i_execute_delete_for(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I execute DELETE for " + url;
		url = customfunction.extractionOfAngleBracketValues(url);
		DTO.runningDefinedMessage = speech + " I execute DELETE for \"" + url+"\"";
		restA.baseURI = url;
	    response = InitRequest.delete(url);
	    strResponseBody = response.getBody().asString();
	    System.out.println(strResponseBody);
	    
	}
	
	
	@Test
	@When("I store the response in {string} as string")
	public void i_store_the_response_in_as_string(String responseAsStr) {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I store the response in \""+responseAsStr+"\" as string";
		DTO.runningDefinedMessage = speech + " I store the response in {"+responseAsStr+"} as string";
		DTO.storeStrings.put(responseAsStr, strResponseBody);
		System.out.println(strResponseBody);
		
		
	}

	@Test
	@When("I store the response in {string} as json")
	public void i_store_the_response_in_as_json(String responseAsJson) throws ParseException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I store the response in \""+responseAsJson+"\" as json";
		DTO.runningDefinedMessage = speech + " I store the response in {"+responseAsJson+"} as json";
		JSONParser parser = new JSONParser();  
		JSONObject jsonResponseBody = (JSONObject) parser.parse(strResponseBody);  
		DTO.storeJsons.put(responseAsJson, jsonResponseBody);
		System.out.println(jsonResponseBody);
		
		
	}
	
	@Test
	@When("I put the jsonExpression {string} from {string} into {string}")
	public void i_put_the_jsonExpression_from_jsonobject_as_var(String jsonExpression, String responseAsJson , String var) throws ParseException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I put the r=jsonpath \""+jsonExpression+"\" from \""+responseAsJson+"\" as \""+var+"\""; 
		DTO.runningDefinedMessage = speech + " I put the r=jsonpath {"+jsonExpression+"} from {"+responseAsJson+"} as {"+var+"}";
		String jstr = DTO.storeJsons.get(responseAsJson).toString();
		System.out.println(jstr);
		JsonNode jsonNode = JsonPath.parse(jstr).read(jsonExpression, JsonNode.class);
		DTO.storeStrings.put(var, jsonNode.toString());
		
		
		
	}
	
	@Test
	@Then("response status should be {string}")
	public void response_status_should_be(String expectedCode) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " response status should be " + expectedCode;
		expectedCode = customfunction.extractionOfAngleBracketValues(expectedCode);
		DTO.runningDefinedMessage = speech + " response status should be \"" + expectedCode+"\"";
		statusCode = Integer.toString(response.getStatusCode());
		System.out.println(statusCode);
		Assert.assertEquals(statusCode , expectedCode);
		
		}
	
	
	
	
}
