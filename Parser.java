
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
		
		public Node(Node left, Node right) {
			this.data = null; // maybe come back to this
			this.left = left;
			this.right = right;
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

	

	public Node parse2(ArrayList<String> tokens, Node curnode) {
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
				ArrayList<String> leftt = new ArrayList<>(left1.subList(0, n-1)); // this is weird, idk why we're doing n-1 so check later
				if (n == tokens.size() - 1) {
					System.out.println("hi");
					parse(leftt, curnode); // MAYBE problem
				}
				Node left = new Node(leftt);
				curnode.left = left;
//				if (n == tokens.size()-2) {
//					return curnode;
//				}
				if (n < tokens.size()) {
					newtoks = new ArrayList<String>(tokens.subList(n+1, tokens.size()));
				}
				curnode.data = prob;
				System.out.println("problem " + tokens.subList(1, n).toString());
				if (n >= tokens.size()) {
					System.out.println("hi?");
					return parse(new ArrayList<String>(tokens.subList(1, n)), left);
				} else {
					System.out.println("yo");
					Node neww = new Node(null);
					Node idk = parse(new ArrayList<String>(tokens.subList(1, n)), left);
					idk.toString();
					neww.left = idk;
					return parse(newtoks, neww); // MAYBE issue
				}
				
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
				if (n == tokens.size() - 1) {
					parse(rightt, curnode);
				}
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
				System.out.println(tokens.subList(1, n).toString());
				if (n >= tokens.size()) {
					return parse(new ArrayList<String>(tokens.subList(1, n)), right);
				} else {
					// here
					System.out.println("hey??");
					Node neww = new Node(null);
					Node idk = parse(new ArrayList<String>(tokens.subList(1, n)), right);
					idk.toString();
					neww.left = idk;
					return parse(newtoks, neww); // MAYBE issue
				}
			} else {
				right1 = new ArrayList<String>(tokens.subList(0, 1));
				Node right = new Node(right1);
				curnode.right = right;
				System.out.println("look here" + tokens.toString());
				if (tokens.size() == 1) { // necessary??
					System.out.println("problem with tokens");
					curnode.data = prob;

					return curnode;
				}
				curnode.data = prob;
				newtoks = new ArrayList<String>(tokens.subList(1, tokens.size()));
				if (newtoks.size() > 0) {
//					ArrayList<String> lst = new ArrayList<String>();
//					lst.addAll(curnode.left.data); lst.addAll(curnode.right.data);
					curnode.data = prob;
					Node newNode = new Node(null);
					newNode.left = curnode;
					return parse(newtoks, newNode);
				}
			}
		}
		
		return parse(newtoks, curnode);
	}
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
	/*
	public Expression parse3(ArrayList<String> tokens) {
		ArrayList<String> prob = new ArrayList<>();
		prob.add("");
		ArrayList<String> newtoks = new ArrayList<>();
		int count = 0;
		for (int j = 0; j < tokens.size(); j++) {
			if (tokens.get(j).equals("(")) {
				int i = 1;
				int n = 1; // n is the index of the last ) when num of  ( = num of )
				int c = 0;
				int o = 1;
				while (i < tokens.size() && c != o) {
					if (tokens.get(i).equals(")")) {
						n = i;
						c++;
					} else if (tokens.get(i).equals("(")) {
						o++;
					}
					i++;
				}
				count++;
			} else {
				count++;
			}
		}
		if (count == 1) {
			if (tokens.size() > 1) {
				return parse(new ArrayList<String>(tokens.subList(1, tokens.size()-1)));
			} else {
			return new Variable(tokens.get(0));
			}
		} else if (count == 2) {
			ArrayList<String> lst1 = new ArrayList<String>(tokens);
			ArrayList<String> lst2 = new ArrayList<String>(new ArrayList<String>(tokens.subList(lst1.size(), tokens.size())));
			return new Application(parse(lst1), parse(lst2)); // maybe change to node
		} else {
			ArrayList<String> lst1 = new ArrayList<String>(tokens);
			ArrayList<String> lst2 = new ArrayList<String>(new ArrayList<String>(tokens.subList(lst1.size(), tokens.size())));
			return new Application (new Application(parse(lst1), parse(lst2)), parse(new ArrayList<String>(tokens.subList(lst1.size() + lst2.size(), tokens.size())));
		}
	}
	*/
	public Node parse(ArrayList<String> tokens) {
		Node curnode = new Node(null);
		ArrayList<String> prob = new ArrayList<>();
		prob.add("");
		int count = 0;
		for (int j = 0; j < tokens.size(); j++) {
			if (tokens.get(j).equals("(")) {
				j = 1;
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
		System.out.println(count);
		if (count == 1) {
			if (tokens.size() > 1) {
				return parse(new ArrayList<String>(tokens.subList(1, tokens.size()-1)));
			} else {
			curnode = new Node(new ArrayList<String>(tokens.subList(0, 1)));
			return curnode;
			}
		} else if (count == 2) {
			ArrayList<String> lst1 = getList(tokens); // PROBLEM: paranthesis are not included as part of list
			ArrayList<String> lst2 = new ArrayList<>();
			if (tokens.get(0).equals("(")) {
				lst2 = getList(new ArrayList<String>(tokens.subList(lst1.size()+2, tokens.size())));
			} else {
				lst2 = getList(new ArrayList<String>(tokens.subList(lst1.size(), tokens.size())));
			}
			System.out.println(lst2);
			return new Node(parse(lst1), parse(lst2)); 
		} else {
			ArrayList<String> lst1 = new ArrayList<String>(getList(tokens));
			ArrayList<String> lst2 = getList(new ArrayList<String>(tokens.subList(lst1.size(), tokens.size())));
			return new Node (new Node(parse(lst1), parse(lst2)), parse(new ArrayList<String>(tokens.subList(lst1.size() + lst2.size(), tokens.size()))));
		}
	}
	/*
	private ArrayList<String> toStringg(Node curnode) { // REPROGRAM TO STRING
		Node def = curnode;
		ArrayList<String> out = new ArrayList<>();
		while (curnode.left != null) {
				if (curnode.left.data != null) {
					out.addAll(0, curnode.left.data);
					//curnode = curnode.left;
					while (curnode.right != null) { // maybe go back to this
						if (curnode.right.data != null) {
							out.addAll(curnode.left.data.size(), curnode.right.data);
							out.addAll(curnode.left.data.size() + curnode.right.data.size(), toStringg(curnode.right)); // i think that's right??
						}
						else {
							toStringg(curnode.right); // MAYBE??
						}
					}
					curnode = curnode.left;
			} else {
				curnode = curnode.left;
			}
		}
		while (def.right != null) { // maybe..
				if (def.right.data != null) {
					out.addAll(def.right.data);
					int size = def.right.data.size();
				
					while (def.left != null) {
						if (def.left.data != null) {
							out.addAll(out.size() - size, def.left.data);
						} else {
							toStringg(curnode.left);
						}
						if (def.right != null) {
							if (def.right.data != null) {
								out.addAll(def.right.data);
								out.addAll(toStringg(def.right));
							}
						}
						def = def.left;
					}
			} else {
				curnode = curnode.left;
			}
		}
		return out;
		
	}
	
	public String toStringggg(Node curnode) {
		ArrayList<String> ar = toStringg(curnode);
		String fn = "(";
		for (int i = 0; i < ar.size(); i++) {
			fn += ar.get(i) + " ";
		}
		return fn.substring(0, fn.length() - 1) + ")";
		
	}
	*/
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
	public String toString (Node curnode) {
		toString2(curnode);
		String fn = "(";
		for (int i = 0; i < retval.size(); i++) {
			fn += retval.get(i) + " ";
		}
		retval = new ArrayList<String>();
		return fn.substring(0, fn.length() - 1) + ")"; // - 1 to get rid of extra space
	}
	
//	public String toString(Expression exp) {
//		String fn = "(";
//		Expression left = exp.left;
//		for (int i )
//	}
//	public Node parse(ArrayList<String> tokens, Node curnode) {
//	return new Node(new ArrayList<String>(tokens.subList(0, 1)));
//	}
	
	//private Variable order ()
	
	public ArrayList<String> preParse(ArrayList<String> tokens) {
		int n;
//		for (int i = 0; i < tokens.size(); i++) {
//			if (tokens.get(i).equals("\\")) {
//				if (i == 0 || !tokens.get(i-1).equals("(")) {
//					tokens.add(i, "(");
//					n = i;
//					while (n < tokens.size() && !tokens.get(n).equals(")")) {
//						n++;
//					}
//					tokens.add(n, ")");
//				}
//			}
//		}
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).equals(".")) {
				if (i == 0 || !tokens.get(i-1).equals("(")) {
					tokens.add(i+1, "(");
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
