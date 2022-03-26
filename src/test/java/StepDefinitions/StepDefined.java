package StepDefinitions;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StepDefined {

	public static void main(String[] args) throws InterruptedException {
		
		WebDriverManager.chromedriver().setup();
		ChromeDriver driver = new ChromeDriver();
		
		WebElement searchField;
		
		//Implicit wait of 60 seconds for all elements
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
		
		driver.get("https://google.com");
		driver.manage().window().maximize();

		searchField = driver.findElement(By.xpath("//input[@title ='Search']"));
		searchField.sendKeys("Jesus");
		JavascriptExecutor jsdriver = (JavascriptExecutor) driver;
		jsdriver.executeScript("window.open()");
		Thread.sleep(4000);
		searchField.sendKeys(Keys.ENTER);
		

	}

}
