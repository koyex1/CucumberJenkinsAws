package StepDefinitions;

import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.core.AbstractMessageSendingTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import Runner.SprApp;
import Utils.Algorithms;
import Utils.ChangingStepSpeech;
import Utils.CucumberFailureResult;
import Utils.FileDownloader;
import Utils.PrintMessages;
import Utils.util;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.plugin.event.Result;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;
import io.cucumber.java.en.Given;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import reactor.core.publisher.Flux;

public class BrowserStepDefinitions {

	public static WebDriver driver;
	public CucumberFailureResult failure = new CucumberFailureResult();
	public util onfailure = new util();
	public static HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	public static Actions act;
	public static int counts;
	public static String attrValue;
	public int currentWindowIndex = -1;
	public Map<String, String> windowKeyValue = new HashMap<String, String>();
	public static String windowName = "";
	public static ChromeOptions options = new ChromeOptions();
	public static Algorithms customfunction = new Algorithms();
	public FileDownloader fileDown;
	public String filepath;
	public Allure allure = null;
	public String speech = "";

	@AfterClass
	@After
	public void closingSetup() {

		if (driver != null) {
			driver.quit();
		}

	}

	

	public void AllureDetails(Scenario scenario) {
		allure.description(scenario.getName());
		scenario.getSourceTagNames().forEach(name -> {
			allure.attachment("issueImage", "http://localhost:3000/failedImage");
			allure.attachment("issueLog", "http://localhost:3000/logReport");
			allure.attachment("issueHtmlPage", "http://localhost:3000/failedHtmlCode");
			allure.epic(name);
			allure.issue("issueImage", "http://localhost:3000/failedImage");
			allure.issue("issueLog", "http://localhost:3000/logReport");
			allure.issue("issueHtmlPage", "http://localhost:3000/failedHtmlCode");
			allure.suite(name);
		});
	}

	@AfterStep
	@AfterMethod
	public void printLog(Scenario scenario) throws IOException, InterruptedException {
		PrintMessages.logMessage(DTO.runningMessage);
		PrintMessages.logMessage(DTO.runningDefinedMessage);
		allure.step(DTO.runningMessage);
		System.out.println("log after here");
		DTO.log.add(DTO.runningMessage);
		// debug report for figuring out cause of failure
		DTO.debugLog.add(DTO.runningMessage);
		DTO.debugLog.add(DTO.runningDefinedMessage);
		System.out.println(DTO.log);

		if (scenario.isFailed()) {
			System.out.println("bullshit.................................................ontest timeout failure");
			System.out.println("please in the name of God print when it fails");
			onfailure.getScreenshot(driver);
			String htmlSourceCode = driver.getPageSource();
			// writing into html file for failed page source code
			FileWriter fWriter = new FileWriter(
					"C:\\Users\\olu\\react\\automationcontrol\\src\\HtmlSourceCode\\failedHtmlCode.html");
			fWriter.write(htmlSourceCode);
			fWriter.close();

			// writing into text for debug log
			DTO.debugLog.add(failure.getFailureMessage(scenario));
			FileWriter writer = new FileWriter(
					"C:\\Users\\olu\\react\\automationcontrol\\src\\logReport\\logReport.txt");
			for (String str : DTO.debugLog) {
				writer.write(str + "\n");
			}
			writer.close();

			FileWriter writer2 = new FileWriter(
					"C:\\Users\\olu\\react\\automationcontrol\\src\\logReport\\logReport.html");
			writer2.write("<body>\n");
			for (String str : DTO.debugLog) {
				writer2.write(str + "<br>\n");
			}
			writer2.write("</body>");
			writer2.close();
			
			this.AllureDetails(scenario);
		}

	}

	@Test
	@Given("browser {string}")
	public void browser(String browser)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		if (browser.isEmpty()) {
			speech = ChangingStepSpeech.speechChange("Given");
			DTO.runningMessage = speech + " browser";
			DTO.runningDefinedMessage = speech + " browser";
		} else {
			speech = ChangingStepSpeech.speechChange("Given");
			DTO.runningMessage = speech + " browser \"" + browser + "\"";
			DTO.runningDefinedMessage = speech + " browser {" + browser + "}";
		}

		if (browser.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", "src\\test\\resources\\Downloads");
			options.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(options);
		} else if (browser.equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browser.equals("internetExplorer")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browser.equals("internetExplorer")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (browser.equals("Safari")) {
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
		} else if (browser.equals("Opera")) {
			WebDriverManager.operadriver().setup();
			driver = new OperaDriver();
		} else {
			driver = new ChromeDriver();
		}
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		windowName = "launchWindow";
		windowKeyValue.put(windowName, driver.getWindowHandle());

	}

	
	@Test
	@Given("variable {string} is {string}")
	public void variable_x_is_y(String variable, String value)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("Given");
		DTO.runningMessage = speech + " variable \"" + variable + "\" is \"" + value + "\"";
		value = customfunction.extractionOfAngleBracketValues(value);
		DTO.runningDefinedMessage = speech + " variable {" + variable + "} is {" + value + "}";
		DTO.storeStrings.put(variable, value);

	}

	@Test
	@When("I fill {string} with value {string}")
	public void i_fill_searchbox_with_value_name(String elementXpath, String value)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I fill \"" + elementXpath + "\" with value \"" + value + "\"";

		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		value = customfunction.extractionOfAngleBracketValues(value);
		DTO.runningDefinedMessage = speech + " I fill {" + elementXpath + "} with value {" + value + "}";
		driver.findElement(By.xpath(elementXpath)).sendKeys(value);

	}

	@Test
	@When("I count {string} as {string}")
	public void i_count_elements_as_count(String elementXpath, String count)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I count \"" + elementXpath + "\" as \"" + count + "\"";
		List<WebElement> elements = driver.findElements(By.xpath(elementXpath));
		int i = 0;
		for (WebElement element : elements) {
			i++;
		}
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.storeStrings.put(count, String.valueOf(i));
		DTO.runningMessage = speech + " I count {" + elementXpath + "} as {" + count + "}";
	}

	@Test
	@When("I click {string}")
	public void i_click(String elementXpath)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I click \"" + elementXpath + "\"";
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.runningDefinedMessage = speech + " I click {" + elementXpath + "}";
		driver.findElement(By.xpath(elementXpath)).click();

	}

	@Test
	@When("I press {string}")
	public void i_press_enter(String key)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I press \"" + key + "\"";
		key = customfunction.extractionOfAngleBracketValues(key);
		DTO.runningDefinedMessage = speech + " I press {" + key + "}";
		if (key.equals("ENTER")) {
			act = new Actions(driver);
			act.sendKeys(Keys.ENTER).perform();
		}

	}

	@Test
	@When("I store its content in {string}")
	public void i_store_its_content_in_var(String downloadVar) throws IOException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I store its content in \"" + downloadVar + "\"";
		File file = new File(filepath);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader conexion = new InputStreamReader(stream, "ISO-8859-1");
		BufferedReader br = new BufferedReader(conexion);
		String st;
		String output = "";
		while ((st = br.readLine()) != null) {
			output = output + "\n" + st;
		}
		DTO.storeStrings.put(downloadVar, output);
		System.out.println(DTO.storeStrings);
		System.out.println(DTO.storeStrings.get(downloadVar));
		DTO.runningMessage = speech + " I store its content in {" + downloadVar + "}";

	}

	@Test
	@When("I record attribute {string} value in {string} as {string}")
	public void i_record_attribute_name_value_in_variable(String attribute, String elementXpath, String var)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I record attribute \"" + attribute + "\" value in \"" + elementXpath
				+ "\" as \"" + var + "\"";
		attribute = customfunction.extractionOfAngleBracketValues(attribute);
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.runningDefinedMessage = speech + " I record attribute {" + attribute + "} value in {" + elementXpath
				+ "} as {" + var + "}";
		attrValue = driver.findElement(By.xpath(elementXpath)).getAttribute(attribute);
		DTO.storeStrings.put(var, attrValue);

	}

	@Test
	@When("I wait for {string} seconds")
	public void i_wait_for_seconds(String time) throws InterruptedException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I wait for \"" + time + "\" seconds";
		time = customfunction.extractionOfAngleBracketValues(time);
		DTO.runningDefinedMessage = speech + " I wait for {" + time + "} seconds";
		Thread.sleep(Integer.valueOf(time) * 1000);

	}

	@Test
	@When("I {string} checkbox {string}")
	public void i_tick_checkbox(String state, String elementXpath)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I \"" + state + "\" checkbox \"" + elementXpath + "\"";
		state = customfunction.extractionOfAngleBracketValues(state);
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.runningDefinedMessage = speech + " I {" + state + "} checkbox {" + elementXpath + "}";
		WebElement actualState = driver.findElement(By.xpath(elementXpath));
		if (!actualState.isSelected() && state.equals("check")) {
			actualState.click();
		} else if (actualState.isSelected() && state.equals("uncheck")) {
			actualState.click();
		}

	}

	@Test
	@When("I drag and drop {string} to {string}")
	public void i_drag_and_drop(String from, String destination) throws InterruptedException, InstantiationException,
			IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I drag and drop \"" + from + "\" to \"" + destination + "\"";
		from = customfunction.extractionOfAngleBracketValues(from);
		destination = customfunction.extractionOfAngleBracketValues(destination);
		DTO.runningDefinedMessage = speech + " I drag and drop {" + from + "} to {" + destination + "}";
		WebElement fromElement = driver.findElement(By.xpath(from));
		WebElement toElement = driver.findElement(By.xpath(destination));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("function createEvent(typeOfEvent) {\n" + "var event =document.createEvent(\"CustomEvent\");\n"
				+ "event.initCustomEvent(typeOfEvent,true, true, null);\n" + "event.dataTransfer = {\n" + "data: {},\n"
				+ "setData: function (key, value) {\n" + "this.data[key] = value;\n" + "},\n"
				+ "getData: function (key) {\n" + "return this.data[key];\n" + "}\n" + "};\n" + "return event;\n"
				+ "}\n" + "\n" + "function dispatchEvent(element, event,transferData) {\n"
				+ "if (transferData !== undefined) {\n" + "event.dataTransfer = transferData;\n" + "}\n"
				+ "if (element.dispatchEvent) {\n" + "element.dispatchEvent(event);\n"
				+ "} else if (element.fireEvent) {\n" + "element.fireEvent(\"on\" + event.type, event);\n" + "}\n"
				+ "}\n" + "\n" + "function simulateHTML5DragAndDrop(element, destination) {\n"
				+ "var dragStartEvent =createEvent('dragstart');\n" + "dispatchEvent(element, dragStartEvent);\n"
				+ "var dropEvent = createEvent('drop');\n"
				+ "dispatchEvent(destination, dropEvent,dragStartEvent.dataTransfer);\n"
				+ "var dragEndEvent = createEvent('dragend');\n"
				+ "dispatchEvent(element, dragEndEvent,dropEvent.dataTransfer);\n" + "}\n" + "\n"
				+ "var source = arguments[0];\n" + "var destination = arguments[1];\n"
				+ "simulateHTML5DragAndDrop(source,destination);", fromElement, toElement);

//act.moveToElement(driver.findElement(By.xpath(from)));
		// act.moveToElement(driver.findElement(By.xpath(from)));
		// act.dragAndDrop(driver.findElement(By.xpath(from)),
		// driver.findElement(By.xpath(destination)));
		// act.perform();

	}

	@Test
	@When("I set slide {string} to axis {string} and {string}")
	public void i_set_slide_to(String elementXpath, String xaxis, String yaxis)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I set slide \"" + elementXpath + "\" to axis \"" + xaxis + "\" and \"" + yaxis
				+ "\"";
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		xaxis = customfunction.extractionOfAngleBracketValues(xaxis);
		yaxis = customfunction.extractionOfAngleBracketValues(yaxis);
		DTO.runningDefinedMessage = speech + " I set slide {" + elementXpath + "} to axis {" + xaxis + "} and {" + yaxis
				+ "}";
		act = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(elementXpath));
		act.moveToElement(element).dragAndDropBy(element, Integer.valueOf(xaxis), Integer.valueOf(yaxis)).perform();

	}

	@Test
	@When("I upload {string} to {string}")
	public void i_upload_file_to(String downloadURL, String elementXpath) throws Exception {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I upload \"" + downloadURL + "\" to \"" + elementXpath + "\"";
		downloadURL = customfunction.extractionOfAngleBracketValues(downloadURL);
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.runningDefinedMessage = speech + " I upload {" + downloadURL + "} to {" + elementXpath + "}";
		WebElement uploadBtn = driver.findElement(By.xpath(elementXpath));
		fileDown = new FileDownloader(driver);
		filepath = fileDown.downloadFile(downloadURL);
		act = new Actions(driver);
		uploadBtn.sendKeys(filepath);

	}

	@Test
	@When("I download file from {string}")
	public void i_download_file_from(String elementXpath) throws Exception {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I download file from \"" + elementXpath + "\"";
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.runningDefinedMessage = speech + " I download file from {" + elementXpath + "}";
		fileDown = new FileDownloader(driver);
		try {
			WebElement uploadBtn = driver.findElement(By.xpath(elementXpath));
			filepath = fileDown.downloadFile(uploadBtn);
		} catch (Exception e) {
			filepath = fileDown.downloadFile(elementXpath);
		}

	}

	@Test
	@When("I scroll to {string}")
	public void i_scroll_to_element(String elementXpath)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I scroll to \"" + elementXpath + "\"";
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.runningDefinedMessage = speech + " I scroll to {" + elementXpath + "}";
		act = new Actions(driver);
		act.moveToElement(driver.findElement(By.xpath(elementXpath))).perform();

	}

	@Test
	@When("I click on {string} every {string} seconds until {string} is shown")
	public void i_click_on_element_until_element2_is_shown(String elementXpath1, String time, String elementXpath2)
			throws InterruptedException, InstantiationException, IllegalAccessException, ClassNotFoundException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException,
			FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I click on \"" + elementXpath1 + "\" every \"" + time + "\" seconds until \""
				+ elementXpath2 + "\" is shown";
		elementXpath1 = customfunction.extractionOfAngleBracketValues(elementXpath1);
		time = customfunction.extractionOfAngleBracketValues(time);
		elementXpath2 = customfunction.extractionOfAngleBracketValues(elementXpath2);
		DTO.runningDefinedMessage = speech + " I click on {" + elementXpath1 + "} every {" + time + "} seconds until {"
				+ elementXpath2 + "} is shown";
		boolean shownYet = false;
		WebElement clickOn = driver.findElement(By.xpath(elementXpath1));

		try {
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			driver.findElement(By.xpath(elementXpath2));
			shownYet = true;
		} catch (Exception e) {
			shownYet = false;
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			clickOn.click();
		}
		if (shownYet == false) {
			i_click_on_element_until_element2_is_shown(elementXpath1, time, elementXpath2);
		} else {
			return;
		}

	}

	@Test
	@When("I visit {string} in window identified by {string}")
	public void i_visit_url_in_window_identified_by_name(String url, String windowName) throws InterruptedException,
			InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I visit \"" + url + "\" in window identified by \"" + windowName + "\"";
		url = customfunction.extractionOfAngleBracketValues(url);
		DTO.runningDefinedMessage = speech + " I visit {" + url + "} in window identified by {" + windowName + "}";
		ArrayList<String> winHandles = new ArrayList<String>(driver.getWindowHandles());
		// url = customfunction.extractionOfAngleBracketValues(url);

		// On Launch
		if (windowKeyValue.containsKey("launchWindow")) {
			windowKeyValue.clear();
			windowKeyValue.put(windowName, driver.getWindowHandle());// why 2. check later
			System.out.println(windowKeyValue);
			driver.get(url);
			String winHandle = driver.getWindowHandle();
			windowKeyValue.put(windowName, winHandle);// why 2
			System.out.println(windowKeyValue);

		}
		// if window already exists
		else if (windowKeyValue.containsKey(windowName)) {
			String winHandle = windowKeyValue.get(windowName);
			driver.switchTo().window(winHandle);
			driver.get(url);
		}

		// window does not exist
		else if (!windowKeyValue.containsKey(windowName)) {
			// open the url in a new tab
			JavascriptExecutor jsdriver = (JavascriptExecutor) driver;
			jsdriver.executeScript("window.open(arguments[0])", url);
			winHandles = new ArrayList<String>(driver.getWindowHandles());
			String winHandle = winHandles.get(winHandles.size() - 1);
			windowKeyValue.put(windowName, winHandle);
			// switch to new tab
			driver.switchTo().window(winHandle);

		}

	}

	@Test
	@When("I close window {string}")
	public void i_close_window_windowName(String windowName) {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I close window \"" + windowName + "\"";
		DTO.runningDefinedMessage = speech + " I close window {" + windowName + "}";
		String winHandle = windowKeyValue.get(windowName);
		driver.switchTo().window(winHandle);
		driver.close();
		windowKeyValue.remove(windowName);

	}

	@Test
	@When("I switch to window {string}")
	public void i_switch_to_windowName(String windowName) {
		speech = ChangingStepSpeech.speechChange("When");
		DTO.runningMessage = speech + " I switch to window \"" + windowName + "\"";
		DTO.runningDefinedMessage = speech + " I switch to window {" + windowName + "}";
		String winHandle = windowKeyValue.get(windowName);
		driver.switchTo().window(winHandle);

	}

	@Test
	@Then("{string} should be shown")
	public void searchresult_should_be_shown(String elementXpath)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("Then");
		DTO.runningMessage = speech + " \"" + elementXpath + "\" should be shown";
		elementXpath = customfunction.extractionOfAngleBracketValues(elementXpath);
		DTO.runningDefinedMessage = speech + " {" + elementXpath + "} should be shown";
		WebElement seen = driver.findElement(By.xpath(elementXpath));
		Assert.assertTrue(seen.isDisplayed());

	}

	@Test
	@Then("{string} should contain {string}")
	public void elements_should_contain_value(String var, String expected)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("Then");
		DTO.runningMessage = speech + " \"" + var + "\" should contain \"" + expected + "\"";
		var = customfunction.extractionOfAngleBracketValues(var);
		expected = customfunction.extractionOfAngleBracketValues(expected);
		DTO.runningDefinedMessage = speech + " {" + var + "} should contain {" + expected + "}";
		// String actual = DTO.storeStrings.get(var);
		Assert.assertTrue(var.contains(expected));

	}

	@Test
	@Then("{string} should match regex {string}")
	public void elements_should_contain_regex_value(String var, String expected)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("Then");
		DTO.runningMessage = speech + " \"" + var + "\" should match regex \"" + expected + "\"";
		var = customfunction.extractionOfAngleBracketValues(var);
		expected = customfunction.extractionOfAngleBracketValues(expected);
		Pattern p = Pattern.compile(expected);
		Matcher m = p.matcher(var);
		DTO.runningDefinedMessage = speech + " {" + var + "} should match regex {" + expected + "}       {Result:"
				+ m.find() + " }";
		Assert.assertTrue(m.find());

	}

	@Test
	@Then("{string} should equals {string}")
	public void elements_should_equals_value(String var, String expected)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("Then");
		DTO.runningMessage = speech + " \"" + var + "\" should equals \"" + expected + "\"";
		var = customfunction.extractionOfAngleBracketValues(var);
		expected = customfunction.extractionOfAngleBracketValues(expected);
		DTO.runningDefinedMessage = speech + " {" + var + "} should equals {" + expected + "}";
		// String actual = DTO.storeStrings.get(var);
		Assert.assertTrue(var.equals(expected));

	}

	// @Test
	// @Then("{string} should equals {int}")
	// public void elements_should_equals_count(String recordedCount, int value) {
	// speech = ChangingStepSpeech.speechChange("Then");
	// DTO.runningMessage = speech + " "+recordedCount+" should equals "+value;
	// DTO.storeIntegers.get(recordedCount);
	//
	// }

	@Test
	@Then("{string} page title should be shown")
	public void page_title_should_be_shown(String pageTitle)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("Then");
		DTO.runningMessage = speech + " \"" + pageTitle + "\" page title should be shown";
		pageTitle = customfunction.extractionOfAngleBracketValues(pageTitle);
		DTO.runningDefinedMessage = speech + " {" + pageTitle + "} page title should be shown";
		String actualTitle = driver.getTitle();
		Assert.assertTrue(actualTitle.contains(pageTitle));

	}

	@Test
	@Then("download should be successful")
	public void download_shoould_be_successful() {
		speech = ChangingStepSpeech.speechChange("Then");
		DTO.runningMessage = speech + " download should be successful";
		Assert.assertTrue(new File(filepath).exists());
		Assert.assertEquals(fileDown.getHTTPStatusOfLastDownloadAttempt(), 200);
		DTO.runningMessage = speech + " download should be successful";

	}

	@Test
	@Then("{string} url should be shown")
	public void url_should_be_shown(String url)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		speech = ChangingStepSpeech.speechChange("Then");
		DTO.runningMessage = speech + " \"" + url + "\" url should be shown";
		url = customfunction.extractionOfAngleBracketValues(url);
		String actualUrl = driver.getCurrentUrl();
		Assert.assertTrue(url.contains(actualUrl));
		DTO.runningMessage = speech + " {" + url + "} url should be shown";

	}
}
