
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
		ArrayList<Character> special = new ArrayList<>();
		special.add('('); special.add(')'); special.add('\\'); special.add('.'); special.add('=');
		String in = "";
		// char[] special = {'(', ')', '\\', '.', '='};
		if (input.equals("")) {
			tokens.add(input);
			return tokens;
		}

		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == ';') {
				return tokens;
			}
			if (special.contains(input.charAt(i))) {
				tokens.add( "" + input.charAt(i));
			} else if (input.charAt(i) == ' ') {
				// skip
			} else {
				while (!special.contains(input.charAt(i)) && input.charAt(i) != ' ' && input.charAt(i) != ';') {
					in += input.charAt(i);
					i++;
				}
				tokens.add(in);
				i --;
				in = "";
			}
		}
		return tokens;
	}



}
