package StepDefinitions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import Utils.Algorithms;
import Utils.FileDownloader;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserStepDefinitions {

	public static WebDriver driver;
	public static HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
	public static Actions act;
	public static int counts;
	public static String attrValue;
	public int currentWindowIndex = -1;
	public Map<String, String> windowKeyValue = new HashMap<String, String>();
	public static String windowName = "";
	public static ChromeOptions options = new ChromeOptions();
	public static Algorithms customfunction = new Algorithms();
	public Map<String, Integer> equalInt = new HashMap<String, Integer>();
	public Map<String, String> equalStr = new HashMap<String, String>();
	public FileDownloader fileDown;
	public String filepath;

	@AfterClass
	public void closingSetup() {
		
		if (driver != null) {
			driver.quit();
		 }
		
	}

	@Test
	@Given("browser {string}")
	public void browser(String string)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		string = customfunction.extractionOfCucumberVariables(string);
		if (string.isEmpty()) {
			System.out.println("Executing Given browser " + string);
		} else {
			System.out.println("Executing Given browser");
		}

		if (string.equals("chrome")) {
			WebDriverManager.chromedriver().setup();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", "src\\test\\resources\\Downloads");
			options.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(options);
		} else if (string.equals("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (string.equals("internetExplorer")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (string.equals("internetExplorer")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		} else if (string.equals("Safari")) {
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
		} else if (string.equals("Opera")) {
			WebDriverManager.operadriver().setup();
			driver = new OperaDriver();
		} else {
			driver = new ChromeDriver();
		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		driver.manage().window().maximize();
		windowName = "launchWindow";
		windowKeyValue.put(windowName, driver.getWindowHandle());

	}

	@Test
	@Given("variable {string} is {string}")
	public void variable_x_is_y(String variable, String value)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		value = customfunction.extractionOfCucumberVariables(value);
		customfunction.storeLocalVaraibles(variable, value);
	}

	@Test
	@When("I fill {string} with value {string}")
	public void i_fill_searchbox_with_value_name(String elementXpath, String value)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		value = customfunction.extractionOfCucumberVariables(value);
		System.out.println("Executing When I fill " + elementXpath + "with value " + value);
		driver.findElement(By.xpath(elementXpath)).sendKeys(value);

	}

	@Test
	@When("I count {string} as {string}")
	public void i_count_elements_as_count(String elementXpath, String count) {
		System.out.println("Executing When I count " + elementXpath + "as " + count);
		List<WebElement> elements = driver.findElements(By.xpath(elementXpath));
		int i = 0;
		for (WebElement element : elements) {
			i++;
		}

		equalInt.put(count, i);
	}

	@Test
	@When("I click {string}")
	public void i_click(String elementXpath) {
		System.out.println("Executing When I Click " + elementXpath);
		driver.findElement(By.xpath(elementXpath)).click();

	}

	@Test
	@When("I press {string}")
	public void i_press_enter(String string) {
		System.out.println("Executing When I press" + string);
		if (string.equals("Enter")) {
			act = new Actions(driver);
			act.sendKeys(Keys.ENTER).perform();
		}

	}

	@Test
	@When("I store its content in {string}")
	public void i_store_its_content_in_var(String downloadVar) throws IOException {
		File file = new File(filepath);
		FileInputStream stream = new FileInputStream(file);
		InputStreamReader conexion = new InputStreamReader(stream, "ISO-8859-1");
		BufferedReader br = new BufferedReader(conexion);
		String st;
		String output="";
		while ((st = br.readLine()) != null) {
			output = output + "\n"+ st;
		}
			equalStr.put(downloadVar, output);
		System.out.println(equalStr);
		System.out.println(equalStr.get(downloadVar));

	}

	@Test
	@When("I record attribute {string} value in {string} as {string}")
	public void i_record_attribute_name_value_in_variable(String attribute, String elementXpath, String var) {
		System.out
				.println("Executing When I record attribute " + attribute + " value in " + elementXpath + " as " + var);
		attrValue = driver.findElement(By.xpath(elementXpath)).getAttribute(attribute);
		equalStr.put(var, attrValue);

	}

	@Test
	@When("I wait for {int} seconds")
	public void i_wait_for_seconds(int time) throws InterruptedException {
		System.out.println("Executing When I wait for " + time + " seconds");
		Thread.sleep(time * 1000);
	}

	@Test
	@When("I {string} checkbox {string}")
	public void i_tick_checkbox(String state, String elementXpath) {
		System.out.println("Executing When I " + state + " checkbox " + elementXpath);
		WebElement actualState = driver.findElement(By.xpath(elementXpath));
		if (!actualState.isSelected() && state.equals("check")) {
			actualState.click();
		} else if (actualState.isSelected() && state.equals("uncheck")) {
			actualState.click();
		}
	}

	@Test
	@When("I drag and drop {string} to {string}")
	public void i_drag_and_drop(String from, String destination) throws InterruptedException {
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
	@When("I set slide {string} to axis {int} and {int}")
	public void i_set_slide_to(String elementXpath, int xaxis, int yaxis) {
		act = new Actions(driver);
		WebElement element = driver.findElement(By.xpath(elementXpath));
		act.moveToElement(element).dragAndDropBy(element, xaxis, yaxis).perform();
	}

	@Test
	@When("I upload {string} to {string}")
	public void i_upload_file_to(String downloadURL, String elementXpath) throws Exception {
		WebElement uploadBtn = driver.findElement(By.xpath(elementXpath));
		// download document to upload first
		fileDown = new FileDownloader(driver);
		filepath = fileDown.downloadFile(downloadURL);
		// WebElement download =driver.findElement(By.xpath(elementXpath));
		// download.click();
		act = new Actions(driver);
		uploadBtn.sendKeys(filepath);
		// act.keyDown(uploadBtn, Keys.CONTROL).sendKeys(filepath).build().perform();
		// File folder = new File("src\\test\\resources\\Downloads");
		// System.out.println(folder.getName());
		// System.out.println(folder.getAbsolutePath());

	}

	@Test
	@When("I download file from {string}")
	public void i_download_file_from(String elementXpath) throws Exception {
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
	public void i_scroll_to_element(String elementXpath) {
		act = new Actions(driver);
		act.moveToElement(driver.findElement(By.xpath(elementXpath))).perform();
	}

	@Test
	@When("I click on {string} every {int} seconds until {string} is shown")
	public void i_click_on_element_until_element2_is_shown(String elementXpath1, int time, String elementXpath2)
			throws InterruptedException {
		boolean shownYet = false;
		WebElement clickOn = driver.findElement(By.xpath(elementXpath1));

		try {
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
			driver.findElement(By.xpath(elementXpath2));
			shownYet = true;
		} catch (Exception e) {
			shownYet = false;
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
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
		System.out.println("Executing When I vist " + url + " in new window identified by " + windowName);
		ArrayList<String> winHandles = new ArrayList<String>(driver.getWindowHandles());
		url = customfunction.extractionOfCucumberVariables(url);
		System.out.println("started from the street now I am here");
		System.out.println("«");
		System.out.println(url);

		// On Launch
		if (windowKeyValue.containsKey("launchWindow")) {
			System.out.println("all window session at this point " + driver.getWindowHandles().toString());
			System.out.println("Testing the window opened for size 0 condition " + windowName);
			windowKeyValue.clear();
			windowKeyValue.put(windowName, driver.getWindowHandle());
			driver.get(url);
			String winHandle = driver.getWindowHandle();
			windowKeyValue.put(windowName, winHandle);
			System.out.println("The only window handle i care about" + winHandle);

		}
		// if window already exists
		else if (windowKeyValue.containsKey(windowName)) {
			System.out.println("all window session at this point " + driver.getWindowHandles().toString());
			System.out.println("Testing the window opened for window already exists condition " + windowName);
			String winHandle = windowKeyValue.get(windowName);
			driver.switchTo().window(winHandle);
			driver.get(url);
			System.out.println("The only window handle i care about" + winHandle);

		}

		// window does not exist
		else if (!windowKeyValue.containsKey(windowName)) {
			System.out.println("all window session at this point " + driver.getWindowHandles());
			System.out.println("Testing the window opened for window does not exist new tab " + windowName);
			JavascriptExecutor jsdriver = (JavascriptExecutor) driver;
			jsdriver.executeScript("window.open(arguments[0])", url);
			System.out.println("all window session at this point after jsexecutor " + driver.getWindowHandles());
			winHandles = new ArrayList<String>(driver.getWindowHandles());
			String winHandle = winHandles.get(winHandles.size() - 1);
			System.out.println("wahala " + winHandle);
			windowKeyValue.put(windowName, winHandle);
			driver.switchTo().window(winHandle);
			System.out.println("The only window handle i care about" + winHandle);

		}

	}

	@Test
	@When("I close window {string}")
	public void i_close_window_windowName(String windowName) {
		System.out.println("all window session at this point " + driver.getWindowHandles().toString());
		System.out.println("Executing When I close window " + windowName);
		String winHandle = windowKeyValue.get(windowName);
		System.out.println("The only window handle i care about" + winHandle);

		driver.switchTo().window(winHandle);
		driver.close();
		windowKeyValue.remove(windowName);

	}

	@Test
	@When("I switch to window {string}")
	public void i_switch_to_windowName(String windowName) {
		System.out.println("all window session at this point " + driver.getWindowHandles().toString());
		System.out.println(windowKeyValue.toString());
		System.out.println("Executing When I switch to " + windowName);
		String winHandle = windowKeyValue.get(windowName);
		System.out.println("The only window handle i care about" + winHandle);
		driver.switchTo().window(winHandle);
	}

	@Test
	@Then("{string} should equals {string}")
	public void elements_should_equals_value(String var, String expected) {
		System.out.println("Executing Then " + var + "should equals " + expected);
		// Assert
		String actual = equalStr.get(var);
		Assert.assertTrue(actual.equals(expected));
	}
	
	@Test
	@Then("{string} should contain {string}")
	public void elements_should_contain_value(String var, String expected) {
		System.out.println("Executing Then " + var + "should equals " + expected);
		// Assert
		String actual = equalStr.get(var);
		Assert.assertTrue(actual.contains(expected));
	}


	@Test
	@Then("{string} should be shown")
	public void searchresult_should_be_shown(String string) {
		System.out.println("Executing Then " + string + " should be shown");
		driver.findElement(By.xpath(string));
	}

	@Test
	@Then("{string} should equals {int}")
	public void elements_should_equals_count(String recordedCount, int value) {
		System.out.println("Executing Then " + counts + "should equals " + value);
		equalInt.get(recordedCount);
		// Assert
	}

	@Test
	@Then("{string} page title should be shown")
	public void page_title_should_be_shown(String pageTitle) {
		System.out.println("Executing Then " + pageTitle + "should be shown");
		String actualTitle = driver.getTitle();
		Assert.assertTrue(actualTitle.contains(pageTitle));

	}

	@Test
	@Then("download should be successful")
	public void download_shoould_be_successful() {

		Assert.assertTrue(new File(filepath).exists());
		Assert.assertEquals(fileDown.getHTTPStatusOfLastDownloadAttempt(), 200);

	}

	@Test
	@Then("{string} url should be shown")
	public void url_should_be_shown(String url) {
		System.out.println("Executing Then " + url + "url should be shown");
		String actualUrl = driver.getCurrentUrl();
		Assert.assertTrue(url.contains(actualUrl));

	}
}
