
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Parser.Node;
import Runner.Runner;

public class Console {
	private static Scanner in;
	
	public static void main(String[] args) {
		in = new Scanner (System.in); // just for fun
		
		Lexer lexer = new Lexer();
		Parser parser = new Parser();
		Map<String, Parser.Node> dict = new HashMap<String, Parser.Node>();
		
		String input = cleanConsoleInput();  // see comment
		
		while (! input.equalsIgnoreCase("exit")) {
			
			ArrayList<String> tokens = lexer.tokenize(input);
			tokens = parser.preParse(tokens);

			String output = "";
			Parser.Node tree = new Parser.Node(null);
			
			try {
				//output = parser.preParse(tokens).toString();
				
			} catch (Exception e) {
				System.out.println("Unparsable expression, input was: \"" + input + "\"");
				input = cleanConsoleInput();
				continue;
			}
			if (tokens.get(0).equals("run")) {
				tokens = tokFix(tokens, dict);
				tree = parser.parse(new ArrayList<String>(tokens.subList(1, tokens.size())));
				Parser.Node ran = Runner.run(tree);
				System.out.println(parser.toString3(ran));
			}
			if (tokens.contains("=")) {
				String varName = "";
				for (int i = 0; i < tokens.indexOf("="); i++) {
					varName += tokens.get(i) + " ";
				}
				varName = varName.substring(0, varName.length() - 1); // gets rid of last space
				if (dict.containsKey(varName)) {
					System.out.println(varName + " is already defined.");
				} else {
					tokens = tokFix(tokens, dict);
					tree = parser.parse(new ArrayList<String>(tokens.subList(tokens.indexOf("=") + 1, tokens.size()))); // PARSING!
					dict.put(tokens.get(0), tree);
					System.out.println("Added " + parser.toString(tree) + " as " + tokens.get(0));
					// this is just a fun extra, it's not meant to be taken seriously
					if (tokens.indexOf("=") != 1) {
						System.out.println("(Hey buddy, just so you know, you can't have spaces in variable names.)");
						System.out.println("Want me to make an exception though, just for you? (y/n)");
						String ans = in.nextLine();
						if (ans.equals("y")) {
							dict.remove(tokens.get(0));
							dict.put(varName, tree);
							System.out.println("Added " + parser.toString(tree) + " as " + varName);
							System.out.println("Just so you know, you're not gonna be able to reference this as part of a bigger function. I have my limits.");
						} else if (ans.equals("n")) {
							System.out.println("Oh. Alright then. Why are you even using this program");
						} else {
							System.out.println("Wow you're REALLY bad at following directions huh");
						}
					}
				}
			} else {
				String temp = "";
				for (int i = 0; i < tokens.size(); i++) {
					temp += tokens.get(i) + " ";
				}
				temp = temp.substring(0, temp.length() - 1); // gets rid of last space
				if (dict.containsKey(temp)) {
					System.out.println(parser.toString(dict.get(temp)));
				} else {
					tokens = tokFix(tokens, dict);
					tree = parser.parse(tokens); // PARSING!
					System.out.println(parser.toString(tree));
				}
				//for testing
//				Parser.Node test = Runner.appfinder(tree); 
//				ArrayList<String> test2 = Runner.appfinder2(tree, new ArrayList<String>());
//				Parser.Node test3 = Runner.funcfinder(tree);
//				System.out.println(parser.toString3(test));
//				System.out.println(test2.toString());
//				Parser.Node test4 = Runner.varreplacer(test3.right.right, test3.left.right.data.get(0), test.left);
////				ArrayList<String> annoying = new ArrayList<>();
////				annoying.add("B");
////				Parser.Node test4 = Runner.varreplacer(test3.right, "x", new Parser.Node(annoying));
//				System.out.println(parser.toString3(test4));
//				Parser.Node test5 = Runner.finalNode(tree, test4, test2);
//				System.out.println(parser.toString(test5));
				//System.out.println(parser.toString3(Runner.run(tree)));
				// next problem: fix to string.
			}
			
			
			input = cleanConsoleInput();
		}
		System.out.println("Goodbye!");
	}
	private static ArrayList<String> tokFix(ArrayList<String> tokens, Map<String, Parser.Node> dict) {
		for (int i = 0; i < tokens.size(); i++) {
			if (dict.containsKey(tokens.get(i))) {
				String n = tokens.get(i);
				//System.out.println((dict.get(n)).ogToks);
				tokens.addAll(i, (dict.get(n)).ogToks);
				
				tokens.remove(i + (dict.get(n)).ogToks.size());
			}

		}
		return tokens;
		
	}

	
	
	/*
	 * Collects user input, and ...
	 * ... does a bit of raw string processing to (1) strip away comments,  
	 * (2) remove the BOM character that appears in unicode strings in Windows,
	 * (3) turn all weird whitespace characters into spaces,
	 * and (4) replace all λs with backslashes.
	 */
	
	private static String cleanConsoleInput() {
		System.out.print("> ");
		String raw = in.nextLine();
		String deBOMified = raw.replaceAll("\uFEFF", ""); // remove Byte Order Marker from UTF

		String clean = removeWeirdWhitespace(deBOMified);
		
		return clean.replaceAll("λ", "\\\\");
	}
	
	
	public static String removeWeirdWhitespace(String input) {
		String whitespace_chars = 
				""           // dummy empty string for homogeneity
				+ "\\u0009"  // CHARACTER TABULATION
				+ "\\u000A"  // LINE FEED (LF)
				+ "\\u000B"  // LINE TABULATION
				+ "\\u000C"  // FORM FEED (FF)
				+ "\\u000D"  // CARRIAGE RETURN (CR)
				+ "\\u0020"  // SPACE
				+ "\\u0085"  // NEXT LINE (NEL) 
				+ "\\u00A0"  // NO-BREAK SPACE
				+ "\\u1680"  // OGHAM SPACE MARK
				+ "\\u180E"  // MONGOLIAN VOWEL SEPARATOR
				+ "\\u2000"  // EN QUAD 
				+ "\\u2001"  // EM QUAD 
				+ "\\u2002"  // EN SPACE
				+ "\\u2003"  // EM SPACE
				+ "\\u2004"  // THREE-PER-EM SPACE
				+ "\\u2005"  // FOUR-PER-EM SPACE
				+ "\\u2006"  // SIX-PER-EM SPACE
				+ "\\u2007"  // FIGURE SPACE
				+ "\\u2008"  // PUNCTUATION SPACE
				+ "\\u2009"  // THIN SPACE
				+ "\\u200A"  // HAIR SPACE
				+ "\\u2028"  // LINE SEPARATOR
				+ "\\u2029"  // PARAGRAPH SEPARATOR
				+ "\\u202F"  // NARROW NO-BREAK SPACE
				+ "\\u205F"  // MEDIUM MATHEMATICAL SPACE
				+ "\\u3000"; // IDEOGRAPHIC SPACE 
		Pattern whitespace = Pattern.compile(whitespace_chars);
		Matcher matcher = whitespace.matcher(input);
		String result = input;
		if (matcher.find()) {
			result = matcher.replaceAll(" ");
		}

		return result;
	}

}
