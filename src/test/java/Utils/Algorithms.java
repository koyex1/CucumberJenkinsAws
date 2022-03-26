package Utils;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Algorithms {

	// Array of variables inside <<>> contained in a string -listOfEnvFunc
	// Array of the values of these variables -refinedVal
	// return string with these values replacing their variables - newStr

	public static Map<String, String> storage = new HashMap<String, String>();

	public void storeLocalVaraibles(String key, String value) {
		storage.put(key, value);
		System.out.println(storage);
	}

	public String extractionOfCucumberVariables(String source)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {
		// scan environment and customFunctions
		char[] sourceCharacters = source.toCharArray();
		List<Character> a = new ArrayList<Character>();
		boolean begin = false;
		int j = 0;
		List<String> listOfEnvFunc = new ArrayList<String>();
		List<String> refinedVal = new ArrayList<String>();

		// Bring out all the values in <<>>
		for (int i = 0; i < sourceCharacters.length; i++) {
			if (sourceCharacters[i] == '«') {
				begin = true;
				i = i + 1;
			}
			if (sourceCharacters[i] == '»') {
				StringBuilder str = new StringBuilder();
				for (Character ch : a) {
					str.append(ch);
				}
				String string = str.toString();
				listOfEnvFunc.add(string);

				begin = false;
				j = 0;
				a = new ArrayList<Character>();

			}
			if (begin) {
				a.add(sourceCharacters[i]);
				j++;
			}

		}
		System.out.println(listOfEnvFunc);
		if (listOfEnvFunc.isEmpty()) {
			return source;
		}
		System.out.println("list of env , function, var");
		System.out.println(listOfEnvFunc);
		List<String> envKeys = new ArrayList<String>();

		// For local var, function and global var search
		for (String envfunc : listOfEnvFunc) {
			String val;

			val = storage.get(envfunc);

			if (val == null) {
				if (envfunc.contains(".")) {
					int startIndex = 0;
					for (int i = 0; i < envfunc.length(); i++) {
						if (envfunc.charAt(i) == '.' || i == envfunc.length() - 1) {
							String str = "";
							str = envfunc.substring(startIndex, i);
							if (i != envfunc.length() - 1)
								startIndex = i + 1;
							if (i == envfunc.length() - 1)
								str = envfunc.substring(startIndex, i + 1);
							envKeys.add(str);

						}

					}
					// call yaml initialization
					System.out.println("Environment Keys in <<>>");
					System.out.println(envKeys.toString());
					InitiateYaml init = new InitiateYaml();
					System.out.println(init.initialization(envKeys));
					val = init.initialization(envKeys);

				}
			}
			if (val == null) {

				try {

					int count = 0;
					int firstBracket = 0;
					int secondBracket = 0;
					String metName = "";
					for (int i = 0; i < envfunc.length(); i++) {
						char ch = envfunc.charAt(i);
						// not a function if ( and ) is greater than 2
						if (ch == '(' || ch == ')') {
							count++;
						}
						if (ch == '(') {
							firstBracket = i;
							if (count == 1) {
								metName = envfunc.substring(0, i);
								System.out.println("Functions Names in <<>>");
								System.out.println(metName);

							}
						}
						if (ch == ')') {
							secondBracket = i;
						}

					}
					if (count != 2 || firstBracket > secondBracket) {
						// not a function
						throw new IllegalArgumentException("Invalid value provided in function");
					}
					// steps----------
					String arguStr = envfunc.substring(firstBracket + 1, secondBracket);
					System.out.println("arguments in function in <<>>");
					System.out.println(arguStr);
					int arguInt = Integer.valueOf(arguStr);
					Object[] obj = { arguInt };
					Class<?> params[] = new Class[obj.length];
					for (int i = 0; i < obj.length; i++) {
						if (obj[i] instanceof Integer) {
							params[i] = Integer.TYPE;
						} else if (obj[i] instanceof String) {
							params[i] = String.class;
						}
						// you can do additional checks for other data types if you want.
					}

					String className = "Utils.FuncEnv";// Class name
					Class<?> cls = Class.forName(className);
					Object _instance = cls.newInstance();
					Method myMethod = cls.getDeclaredMethod(metName, params);
					String randVal = (String) myMethod.invoke(_instance, obj);
					val = randVal;
					System.out.println("the random var");
					System.out.println(randVal);
				} catch (Exception e) {
					throw new NullPointerException(
							"Variable not declared globally or locally, or function does not exist");
				}
				//
			}

			// after searching through local map, global environment and existing functions
			// if something comes up then
			refinedVal.add(val);
		}

		// replacing with the mapped new values
		boolean skip = false;
		j = 0;
		StringBuilder newStr = new StringBuilder();
		for (int i = 0; i < sourceCharacters.length; i++) {
			if (sourceCharacters[i] == '«') {
				skip = true;
			}
			// skip special characters and replace
			if (sourceCharacters[i] == '»') {
				skip = false;
				newStr.append(refinedVal.get(j));
				j++;
			}
			if (!skip) {
				if (sourceCharacters[i] != '»') {
					newStr.append(sourceCharacters[i]);
				}
			}

		}

		System.out.println("lets see what we have here");
		System.out.println(newStr);

		return newStr.toString();

	}

}