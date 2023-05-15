
import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
	
	public static class Node{
		Variable data;
		Node left;
		Node right;
		public Node(Variable data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}
	}
	public Node root = null;
	
	/*
	 * Turns a set of tokens into an expression.  Comment this back in when you're ready.
	 */
	public Expression parse(ArrayList<String> tokens) throws ParseException {
		if (tokens.get(0).equals("(")) {
			int i = 0;
			ArrayList<String> right = new ArrayList<String>();
			while (!tokens.get(i).equals(")")) {
				right.add(tokens.get(i));
				i++;
			}
			
		}
		return (Expression) root;
	}
	
	//private Variable order ()
	
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
