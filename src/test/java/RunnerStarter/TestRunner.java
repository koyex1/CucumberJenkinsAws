package RunnerStarter;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src\\test\\resources\\Features\\E2eBoard.feature",
				plugin= "io.qameta.allure.cucumber6jvm.AllureCucumber6Jvm",
				glue = {"StepDefinitions"},
				monochrome = true
				)
public class TestRunner extends AbstractTestNGCucumberTests {

}

