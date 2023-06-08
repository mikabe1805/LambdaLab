
import java.text.ParseException;
import java.util.ArrayList;

public class Parser {
	
	public static class Node{
		ArrayList<String> data;
		Node left;
		Node right;
		ArrayList<String> ogToks = new ArrayList<String>();
		String type = "";
		public Node(ArrayList<String> data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}
		
		public Node(Node left, Node right) {
			this.data = null; // maybe come back to this
			this.left = left;
			this.right = right;
		}
		public String toString() {
			return "(" + left.data + " " + right.data + ")";
		}
		
//		ArrayList<Boolean> maybe = new ArrayList<>();
//		public void containss (Node curnode, String str) {
//			if (curnode.data.get(0).equals(str)) {
//				maybe.add(true);
//				return;
//			}
//			if (curnode.left == null && curnode.right == null) {
//				maybe.add(false);
//				return;
//			}
//			if (curnode.left != null) {
//				containss(curnode.left, str);
//			} if (curnode.right != null) {
//				containss (curnode.right, str);
//			}
//		}
//		public boolean contains (Node curnode, String str) {
//			containss(curnode, str);
//			if (maybe.contains(true)) {
//				maybe = new ArrayList<>();
//				return true;
//			} else {
//				maybe = new ArrayList<>();
//				return false;
//			}
//		}
	}
	
	/*
	 * Turns a set of tokens into an expression.  Comment this back in when you're ready.
	 */
	private ArrayList<String> getList(ArrayList<String> toks) {
		ArrayList<String> finall = new ArrayList<String>();
		if (toks.get(0).equals("(")) {
			int i = 1;
			int n = 1; // n is the index of the last ) when num of  ( = num of )
			int c = 0;
			int o = 1;
			while (i < toks.size() && c != o) {
				finall.add(toks.get(i));
				if (toks.get(i).equals(")")) {
					n = i;
					c++;
				} else if (toks.get(i).equals("(")) {
					o++;
				}
				i++;
			}
			finall = new ArrayList<>(finall.subList(0, n-1));
		} else {
			finall.add(toks.get(0));
		}
		
		return finall;
	}
	ArrayList<String> realToks = new ArrayList<String>();
	int cnt = 0;
	public Node parse(ArrayList<String> tokens) {
		Node curnode = new Node(null);
		if (cnt == 0) {
			realToks.addAll(tokens);
			cnt++;
		}
		ArrayList<String> prob = new ArrayList<>();
		prob.add("");
		int count = 0;
		for (int j = 0; j < tokens.size(); j++) {
			if (tokens.get(j).equals("(")) {
				j++;
				int n = 1; // n is the index of the last ) when num of  ( = num of )
				int c = 0;
				int o = 1;
				while (j < tokens.size() && c != o) {
					if (tokens.get(j).equals(")")) {
						n = j;
						c++;
					} else if (tokens.get(j).equals("(")) {
						o++;
					}
					j++;
				}
				j--;
				count++;
			} else {
				count++;
			}
		}
		//System.out.println(count);
		if (count == 1) {
			if (tokens.size() > 1) {
				return parse(new ArrayList<String>(tokens.subList(1, tokens.size()-1)));
			} else {
			curnode = new Node(new ArrayList<String>(tokens.subList(0, 1)));
			curnode.ogToks.addAll(realToks);
			curnode.type = "V";
			return curnode;
			}
		} else if (count == 2) {
			ArrayList<String> lst1 = getList(tokens); 
			ArrayList<String> lst2 = new ArrayList<>();
			if (tokens.get(0).equals("(")) {
				lst2 = getList(new ArrayList<String>(tokens.subList(lst1.size()+2, tokens.size())));
			} else {
				lst2 = getList(new ArrayList<String>(tokens.subList(lst1.size(), tokens.size())));
			}
			//System.out.println(lst2);
			curnode = new Node(parse(lst1), parse(lst2)); 
			curnode.ogToks.addAll(realToks);
			curnode.type = "A";
			return curnode;
		} else { // this could potentially be a function. I'm gonna split it into 2 scenarios to take account of funcitons
			// cur problem: app of \x is getting assigned func
			int parnum = 0;
			ArrayList<String> lst1 = new ArrayList<String>(getList(tokens)); // maybe what you should do is check for the thing that goes in if it has the app w \x
			if (tokens.get(0).equals("(")) {
				parnum += 2;
			}
			ArrayList<String> lst2 = getList(new ArrayList<String>(tokens.subList(lst1.size() + parnum, tokens.size())));
			if (tokens.get(lst1.size() + parnum).equals("(")) {
				parnum += 2;
			}
			Node node2 = new Node(parse(lst1), parse(lst2));
			node2.ogToks.addAll(realToks);
			node2.type = "A";
			curnode = new Node (node2, parse(new ArrayList<String>(tokens.subList(lst1.size() + lst2.size() + parnum, tokens.size()))));
			curnode.ogToks.addAll(realToks);
			if (lst1.contains("\\")) {
				curnode.type = "F";
			} else {
				curnode.type = "A";
			}
			return curnode;
		}
	}
	
	ArrayList<String> retval = new ArrayList<String>();
	public void toString2 (Node curnode) {
		if (curnode.left == null && curnode.right == null) {
			retval.addAll(curnode.data);
			return;
		} if (curnode.left != null) {
			toString2 (curnode.left);
		} if (curnode.right != null) {
			toString2 (curnode.right);
		}
	}
	
	public String toString3 (Node curnode) {
		toString2(curnode);
		String fn = "(";
		for (int i = 0; i < retval.size(); i++) {
			fn += retval.get(i) + " ";
		}
		retval = new ArrayList<String>();
		return fn.substring(0, fn.length() - 1) + ")"; // - 1 to get rid of extra space
	}
	
	public String toString (Node curnode) {
		ArrayList<String> done = curnode.ogToks;
		if (!done.get(0).equals("(")) {
			String fn = "(";
			for (int i = 0; i < done.size(); i++) {
				fn += done.get(i) + " ";
			}
			return fn.substring(0, fn.length() - 1) + ")"; // - 1 to get rid of extra space
		} else {
			String fn = "";
			for (int i = 0; i < done.size(); i++) {
				fn += done.get(i) + " ";
			}
			return fn.substring(0, fn.length() - 1); // - 1 to get rid of extra space
		}
	}
	
	public ArrayList<String> preParse(ArrayList<String> tokens) {
		int n;
		cnt = 0;
		realToks.clear();
		// wrap expression in parens
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).equals("\\")) {
				if (i == 0 || !tokens.get(i-1).equals("(")) {
					tokens.add(i, "(");
					n = i;
					int o = 0;
					int c = 0;
					while (n < tokens.size() && c <= o) {
						if (tokens.get(n).equals("(")) {
							o++;
						} else if (tokens.get(n).equals(")")) {
							c++;
						}
						n++;
					}
					tokens.add(n, ")");
				}
			}
		}
		// eat up things to the right
		for (int i = 0; i < tokens.size(); i++) { // go back to this... a little messed up :( figure out multi paren rule
			if (tokens.get(i).equals(".")) {
				if (i == 0 || !tokens.get(i+1).equals("(")) {
					tokens.add(i+1, "(");
					n = i+2;
					int o = 1;
					int c = 0;
					while (n < tokens.size() && c < o) { // PROBLEM TO FIX, reason \a.a works is bc exit condition not c and o like meant
						//System.out.println(tokens.get(n));
						if (tokens.get(n).equals("(")) {
							o++;
						} else if (tokens.get(n).equals(")") && c+1 < o) {
							c++;
							tokens.add(i+1, "(");
							tokens.add(n+1, ")");
							n+= 2;
						} else if (tokens.get(n).equals(")")) {
							c++;
						}
						n++;
					}
					tokens.add(n, ")");
				}
			}
		}
		return tokens;
	}
}
