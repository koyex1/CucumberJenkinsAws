package Runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

//@RunWith(Cucumber.class)
@CucumberOptions(features = "src\\test\\resources\\Features",
				glue = {"StepDefinitions"},
				monochrome = true
				)
public class TestRunner extends AbstractTestNGCucumberTests {

}

