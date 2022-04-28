package RunnerStarter;

import Runner.SprApp;
import io.cucumber.core.cli.Main;
import io.cucumber.testng.AbstractTestNGCucumberTests;

public class UseCukeFromMainTest{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//SprApp.main(args);
			System.out.println("mvn is in.........");
			Main.main(new String[] {"-g","StepDefinitions","src\\main\\resources\\Features\\E2eBoard.feature"});
		}

	}
