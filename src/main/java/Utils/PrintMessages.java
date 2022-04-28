package Utils;

import java.util.Date;

public class PrintMessages {
	
	
	
	public static void logMessage(String message) {
		Date date = new Date();
		message = "Executing... " + message;
		System.out.println(date + ": " + message);
	}

}
