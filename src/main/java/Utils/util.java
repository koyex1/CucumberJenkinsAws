package Utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;


public class util {
	
	public void getScreenshot(WebDriver driver) throws IOException {
		System.out.println("bullshit for ever.................................................getscreenshot");
		Date currentdate = new Date();
		String screenshotfilename = currentdate.toString().replace(" ", "-").replace(":","-");
		System.out.println(screenshotfilename);
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		//TODO: use the same filename to replace existing filename so images do not pile up
		FileUtils.copyFile(screenshotFile, new File("C:\\Users\\olu\\react\\automationcontrol\\src\\Screenshots\\" + "failedImage" + ".png"));
		
	}

}
