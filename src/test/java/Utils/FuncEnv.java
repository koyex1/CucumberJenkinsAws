package Utils;

public class FuncEnv {
	public String generationOfRandomNames(int numberOfCharacters) {
		int min = 0;
		int max = 25;
		StringBuilder str = new StringBuilder();
		String alphabets = "abcdefghijklmnopqrstuvwxyz";
		for(int i= 0; i<numberOfCharacters; i++) {
			int randValue = (int) Math.floor(Math.random()*(max-min+1)+min);
			char ch = alphabets.charAt(randValue);
				str.append(ch);
			
		}
		System.out.println(str);
		return str.toString();

	}
}
