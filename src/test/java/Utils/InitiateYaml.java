package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class InitiateYaml {
	
	public String initialization(List<String> envKeys) throws FileNotFoundException {
		InputStream inputStream = new FileInputStream(new File("src\\test\\java\\environmentVariables.yaml"));

		//Yaml yaml = new Yaml(new Constructor(EnvData.class));
		//EnvData data = yaml.load(inputStream);
		//System.out.println(data);
		Yaml yaml = new Yaml();
		Map<String, Object> data = yaml.load(inputStream);
		
		for(String key: envKeys) {
			if(data.get(key) instanceof String ) {
				return (String) data.get(key);
				
			}
			else if ((data.get(key) instanceof Integer)){
				return data.get(key).toString();
			}
			else {
				data=  (Map<String, Object>) data.get(key);
			}
		}
		System.out.println("YAML output.. Genious");
		System.out.println(data);
		throw new FileNotFoundException();
	}
	


}
