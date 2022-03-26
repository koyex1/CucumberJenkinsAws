package StepDefinitions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Experiments {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		Map<String, Object> test = new HashMap<String, Object>();
		test.put("dsfs", test);
		System.out.println(test.get("this"));
		
		InputStream inputStream = new FileInputStream(new File("src\\test\\java\\environmentVariables.yaml"));

		Yaml yaml = new Yaml();
		Map<String, Object> data = yaml.load(inputStream);
		System.out.println(data.get("env"));
	}

}
