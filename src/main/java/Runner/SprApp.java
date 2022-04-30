package Runner;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;

import StepDefinitions.DTO;
import io.cucumber.core.cli.Main;

@Lazy
@SpringBootApplication(scanBasePackages = { "StepDefinitions", "Runner", "Controller" })
@EnableScheduling
public class SprApp {

	public static void main(String[] args) throws InterruptedException {
		SpringApplication app = new SpringApplication(SprApp.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", "8083"));
		app.run(args);
		// my server starts in give or take 5seconds
		// take note of this 5seconds and implement this 5 seconds delay on my frontend
		// post request that sends the key to backend
		// then put a delay in backend to
		Thread.sleep(20000);
		System.out.println("I hope I see you");
		System.out.println(DTO.keyValue);
		Main.main(new String[] { "-g", "StepDefinitions",
				"src\\main\\resources\\Features\\" + DTO.keyValue + ".feature" });
	}
}
