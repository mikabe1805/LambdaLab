
import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
	
	/*
	 * Turns a set of tokens into an expression.  Comment this back in when you're ready.
	 */
	public Expression parse(ArrayList<String> tokens) throws ParseException {
		Variable var = new Variable(tokens.get(0));
		
		// This is nonsense code, just to show you how to thrown an Exception.
		// To throw it, type "error" at the console.
		if (var.toString().equals("error")) {
			throw new ParseException("User typed \"Error\" as the input!", 0);
		}
		
		return var;
	}
	
	public ArrayList<String> preParse(ArrayList<String> tokens) {
		int n;
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).equals("\\")) {
				if (i == 0 || !tokens.get(i-1).equals("(")) {
					tokens.add(i, "(");
					n = i;
					while (n < tokens.size() && !tokens.get(n).equals(")")) {
						n++;
					}
					tokens.add(n, ")");
				}
			}
		}
		return tokens;
	}
}
