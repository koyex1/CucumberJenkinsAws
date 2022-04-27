package StepDefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;

import lombok.Getter;
import lombok.Setter;

public class DTO {
	
	
	public static String runningMessage;
	public static String runningDefinedMessage;
	public static String CurrentStepSpeech= "";
	public static List<String> log = new ArrayList<String>();
	//
	public static Map<String, String> storeStrings = new HashMap<String, String>();
	public static Map<String, JSONObject> storeJsons = new HashMap<String, JSONObject>();
	public static String keyValue;
	public static List<String> debugLog = new ArrayList<String>();

}
