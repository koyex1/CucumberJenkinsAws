package Utils;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import StepDefinitions.DTO;

public class Algorithms {

	// Array of variables inside <<>> contained in a string -ArrayListOfLocEnvFunc
	// Array of the values of these variables - arrayListOfReplacedValues
	// return string with these values replacing their variables - newStr


	// STRIPS THE STRING OFF ITS <<>> AND CHECKS FIRST IF THE VAR IN
	// <<>> POINTS FIRST TO A LOCAL VARIABLE, THEN NEXT IS THE YAML FILE, THEN NEXT
	// IS A FUNCTION
	public String extractionOfAngleBracketValues(String source)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException, InvocationTargetException, FileNotFoundException {

		int j = 0;
		boolean begin = false;
		char[] sourceCharacters = source.toCharArray();
		List<Character> a = new ArrayList<Character>();
		List<String> ArrayListOfLocsEnvsFuncs = new ArrayList<String>();
		List<Object> arrayListOfReplacedValues = new ArrayList<Object>();

		System.out.println(source); // prints the original gherkins {string}
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
				ArrayListOfLocsEnvsFuncs.add(string);

				begin = false;
				j = 0;
				a = new ArrayList<Character>();

			}
			if (begin) {
				a.add(sourceCharacters[i]);
				j++;
			}

		}
		System.out.println("Array List of local, environment or function variables");
		System.out.println(ArrayListOfLocsEnvsFuncs); // printing of ARRAY LIST of all the variables found inside <<>>
		if (ArrayListOfLocsEnvsFuncs.isEmpty()) {
			return source;
		}

		List<String> envKeys = new ArrayList<String>();

		// For local variables, functions and global variable search
		for (String locEnvFunc : ArrayListOfLocsEnvsFuncs) {
			String val;
			
			
			//Search LOCALLY declared variables
	/*1*/	val = DTO.storeStrings.get(locEnvFunc);


			//Search Environment
			if (val == null) {
				if (locEnvFunc.contains(".")) {
					int startIndex = 0;
					for (int i = 0; i < locEnvFunc.length(); i++) {
						if (locEnvFunc.charAt(i) == '.' || i == locEnvFunc.length() - 1) {
							String str = "";
							str = locEnvFunc.substring(startIndex, i);
							if (i != locEnvFunc.length() - 1)
								startIndex = i + 1;
							if (i == locEnvFunc.length() - 1)
								str = locEnvFunc.substring(startIndex, i + 1);
							envKeys.add(str);

						}

					}
					
					//environment unlike the rest produces arraylist of values that will be used in initiateyaml class which has a while loop
					System.out.println("Array list of Environment Keys");
					System.out.println(envKeys.toString());
					InitiateYaml init = new InitiateYaml();
					System.out.println("value picked out from environment keys");
					System.out.println(init.initialization(envKeys));
			/*2*/	val = init.initialization(envKeys);

				}
			}
			
			//SEARCH functions
			if (val == null) {

				try {

					int count = 0;
					int firstBracket = 0;
					int secondBracket = 0;
					String methodName = "";
					for (int i = 0; i < locEnvFunc.length(); i++) {
						char ch = locEnvFunc.charAt(i);
						// not a function if ( and ) is greater than 2
						if (ch == '(' || ch == ')') {
							count++;
						}
						if (ch == '(') {
							firstBracket = i;
							if (count == 1) {
								methodName = locEnvFunc.substring(0, i);
								System.out.println("Functions Names in");
								System.out.println(methodName);

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
					String arguStr = locEnvFunc.substring(firstBracket + 1, secondBracket);
					System.out.println("arguments in function");
					System.out.println(arguStr);
					int arguInt = Integer.valueOf(arguStr);
					
					
					//Calling the function starts here
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
					Method myMethod = cls.getDeclaredMethod(methodName, params);
					String funcReturnedVal = (String) myMethod.invoke(_instance, obj);
			/*3*/	val = funcReturnedVal;
					System.out.println("the random var");
					System.out.println(funcReturnedVal);
				} catch (Exception e) {
					val = locEnvFunc;
				}
				//
			}

			// after searching through local map, global environment and existing functions
			// if something comes up then
			arrayListOfReplacedValues.add(val);
		}

		// replacing with the mapped new values
		j = 0;
		boolean skip = false;
		
		StringBuilder replaceSource = new StringBuilder();
		for (int i = 0; i < sourceCharacters.length; i++) {
			if (sourceCharacters[i] == '«') {
				skip = true;
			}
			// skip special characters and replace
			if (sourceCharacters[i] == '»') {
				skip = false;
				replaceSource.append(arrayListOfReplacedValues.get(j));
				j++;
			}
			if (!skip) {
				if (sourceCharacters[i] != '»') {
					replaceSource.append(sourceCharacters[i]);
				}
			}

		}

		System.out.println("replaces Source");
		System.out.println(replaceSource);

		return replaceSource.toString();

	}

}