
import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
	
	public static class Node{
		ArrayList<String> data;
		Node left;
		Node right;
		public Node(ArrayList<String> data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}
		public String toString() {
			return "(" + left.data + " " + right.data + ")";
		}
	}
	
	/*
	 * Turns a set of tokens into an expression.  Comment this back in when you're ready.
	 */
	/*
	public Expression parse(ArrayList<String> tokens) throws ParseException {
		ArrayList<String> newtoks = new ArrayList<>();
		if (tokens.size() == 0) {
			return curnode;
		} else if (curnode.left == null) {
			ArrayList<String> left1 = new ArrayList<>();
			
			if (tokens.get(0).equals("(")) {
				int i = 1;
				while (!tokens.get(i).equals(")")) {
					left1.add(tokens.get(i));
					i++;
				}
				newtoks.addAll(tokens.subList(i+1, tokens.size()));
				
			} else {
				left1 = (ArrayList<String>) tokens.subList(0, 1);
				newtoks.addAll(tokens.subList(1, tokens.size()));
			}
			Node left = new Node(left1);
			curnode.left = left;
		} else if (curnode.right == null) {
			ArrayList<String> right1 = new ArrayList<>();

			if (tokens.get(0).equals("(")) {
				int i = 1;
				while (!tokens.get(i).equals(")")) {
					right1.add(tokens.get(i));
					i++;
				}
				newtoks.addAll(tokens.subList(i+1, tokens.size()));
				
			} else {
				right1 = (ArrayList<String>) tokens.subList(0, 1);
				newtoks.addAll(tokens.subList(1, tokens.size()));
			}
			Node right = new Node(right1);
			curnode.right = right;
			if (newtoks.size() > 0) {
				Node newNode = new Node(null);
				newNode.left = curnode;
			}
		}
		
		return parse(newtoks, curnode);
	}
	
	private 
	*/
	

	public Node parse(ArrayList<String> tokens, Node curnode) {
		ArrayList<String> prob = new ArrayList<>();
		prob.add("");
		ArrayList<String> newtoks = new ArrayList<>();
		if (tokens.size() == 0) {
			return curnode;
		} else if (curnode.left == null) {
			ArrayList<String> left1 = new ArrayList<>();
			if (tokens.get(0).equals("(")) {
				int i = 1;
				int n = 1; // n is the index of the last ) when num of  ( = num of )
				int c = 0;
				int o = 1;
				while (i < tokens.size() && c != o) {
					left1.add(tokens.get(i));
					if (tokens.get(i).equals(")")) {
						n = i;
						c++;
					} else if (tokens.get(i).equals("(")) {
						o++;
					}
					i++;
				}
				ArrayList<String> leftt = new ArrayList<>(left1.subList(0, n-1));
				Node left = new Node(leftt);
				curnode.left = left;
//				if (n == tokens.size()-2) {
//					return curnode;
//				}
				if (n < tokens.size()) {
					newtoks = new ArrayList<String>(tokens.subList(n+1, tokens.size()));
				}
				curnode.data = prob;
				parse(new ArrayList<String>(tokens.subList(1, n)), left);
				
			} else {
				left1 = new ArrayList<String>(tokens.subList(0, 1));
				Node left = new Node(left1);
				curnode.left = left;
				
				if (tokens.size() == 1) {
					curnode.data = prob;

					return curnode;
				}
				curnode.data = prob;
				newtoks = new ArrayList<String>(tokens.subList(1, tokens.size()));
			}
		} else if (curnode.right == null) {
			ArrayList<String> right1 = new ArrayList<>();

			if (tokens.get(0).equals("(")) {
				int i = 1;
				int n = 1; // n is the index of the last ) when num of  ( = num of )
				int c = 0;
				int o = 1;
				while (i < tokens.size() && c != o) {
					right1.add(tokens.get(i));
					if (tokens.get(i).equals(")")) {
						n = i;
						c++;
					} else if (tokens.get(i).equals("(")) {
						o++;
					}
					i++;
				}
				ArrayList<String> rightt = new ArrayList<>(right1.subList(0, n-1));
				Node right = new Node(rightt);
				curnode.right = right;
//				if (n == tokens.size()-1) {
//					curnode.data = prob;
//
//					return curnode;
//				}
				if (n < tokens.size()) {
					newtoks = new ArrayList<String>(tokens.subList(n+1, tokens.size()));
				}
				curnode.data = prob;
				parse(new ArrayList<String>(tokens.subList(1, n)), right);
			} else {
				right1 = new ArrayList<String>(tokens.subList(0, 1));
				Node right = new Node(right1);
				curnode.right = right;
				if (tokens.size() == 1) {
					curnode.data = prob;

					return curnode;
				}
				curnode.data = prob;
				newtoks = new ArrayList<String>(tokens.subList(1, tokens.size()));
			}
			
			if (newtoks.size() > 0) {
//				ArrayList<String> lst = new ArrayList<String>();
//				lst.addAll(curnode.left.data); lst.addAll(curnode.right.data);
				curnode.data = prob;
				Node newNode = new Node(null);
				newNode.left = curnode;
				return parse(newtoks, newNode);
			}
		}
		
		return parse(newtoks, curnode);
	}
	
	private ArrayList<String> toStringg(Node curnode) {
		Node def = curnode;
		ArrayList<String> out = new ArrayList<>();
		if (curnode.left != null) {
				if (curnode.left.data != null) {
				out.addAll(0, curnode.left.data);
				curnode = curnode.left;
				while (curnode.left != null) {
					out.addAll(0, curnode.left.data);
					if (curnode.right != null) {
						if (curnode.right.data != null) {
							out.addAll(curnode.left.data.size(), curnode.right.data);
							out.addAll(curnode.left.data.size() + curnode.right.data.size(), toStringg(curnode.right));
						}
					}
					curnode = curnode.left;
				}
			}
		}
		if (def.right != null) {
				if (def.right.data != null) {
					out.addAll(def.right.data);
					int size = def.right.data.size();
					def = def.right;
				
					while (def.left != null) {
						out.addAll(out.size() - size, def.left.data);
						if (def.right != null) {
							if (def.right.data != null) {
								out.addAll(def.right.data);
								out.addAll(toStringg(def.right));
							}
						}
						def = def.left;
				}
			}
		}
		return out;
		
	}
	
	public String toString(Node curnode) {
		ArrayList<String> ar = toStringg(curnode);
		String fn = "(";
		for (int i = 0; i < ar.size(); i++) {
			fn += ar.get(i) + " ";
		}
		return fn.substring(0, fn.length() - 1) + ")";
		
	}
	
//	public Node parse(ArrayList<String> tokens, Node curnode) {
//	return new Node(new ArrayList<String>(tokens.subList(0, 1)));
//	}
	
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
