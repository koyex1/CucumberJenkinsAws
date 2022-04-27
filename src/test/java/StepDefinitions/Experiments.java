package StepDefinitions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.filter.ValueNodes.JsonNode;

import Utils.Algorithms;
import Utils.FuncEnv;
import Utils.InitiateYaml;

public class Experiments {

	public static void main(String[] args) throws FileNotFoundException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		//Map<String, Object> test = new HashMap<String, Object>();
		//test.put("dsfs", test);
		//System.out.println(test.get("this"));
		
		//InputStream inputStream = new FileInputStream(new File("src\\test\\java\\environmentVariables.yaml"));

		//Yaml yaml = new Yaml();
		//Map<String, Object> data = yaml.load(inputStream);
		//System.out.println(data.get("env"));
		Algorithms u = new Algorithms();
		System.out.println(u.extractionOfAngleBracketValues("«environment.Testing Env.first URL»"));
		//String jstr = "[{\"firstName\":\"Olumide\"},{\"firstName\":\"Gary \"},{\"firstName\":\"Frank\"},{\"firstName\":\"Kun\"},{\"firstName\":\"Sadio\"},{\"firstName\":\"Harry\"},{\"firstName\":\"Van\"},{\"firstName\":\"Lionel \"},{\"firstName\":\"Christiano\"}]";
		//JsonNode jsonNode = JsonPath.parse(jstr).read("$[0].firstName", JsonNode.class);
		//System.out.println(jsonNode);
		
	}

}
