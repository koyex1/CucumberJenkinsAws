package RunnerStarter;

import Runner.SprApp;
import io.cucumber.core.cli.Main;

public class UseCukeFromMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//SprApp.main(args);
		Main.main(new String[] {"-g","StepDefinitions","src\\test\\resources\\Features\\E2eBoard.feature"});
	}

}
