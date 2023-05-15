
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
	
	/*
	 * A lexer (or "tokenizer") converts an input into tokens that
	 * eventually need to be interpreted.
	 * 
	 * Given the input 
	 *    (\bat  .bat flies)cat  λg.joy! )
	 * you should output the ArrayList of strings
	 *    [(, \, bat, ., bat, flies, ), cat, \, g, ., joy!, )]
	 *
	 */
	public ArrayList<String> tokenize(String input) {
		ArrayList<String> tokens = new ArrayList<String>();

//		for (int i = 0; i < input.length(); i++) {
//			if (input.charAt(i) == ';') {
//				break;
//			}
//			if (input.charAt(i) == '(' || input.charAt(i) == ')' || input.charAt(i) == '\' 
//					|| input.charAt(i) == '.' || input.charAt(i) == '=')
//			
//		}

		tokens.add(input);
		return tokens;
	}



}
