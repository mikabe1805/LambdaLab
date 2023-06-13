
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
		//System.out.println(finall.toString());
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
				int n = j; // n is the index of the last ) when num of  ( = num of )
				int c = 0;
				int o = 1;
				while (j < tokens.size() && c < o) {
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
			//System.out.println("V: " + curnode.data);
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
			//System.out.println("list 1: " + lst1 + " list 2: " + lst2);
			//System.out.println(lst2);
			curnode = new Node(parse(lst1), parse(lst2)); 
			curnode.ogToks.addAll(realToks);
			curnode.type = "A";
//			if (curnode.left != null && curnode.right != null && curnode.left.data != null && curnode.right.data != null) {
//				System.out.println(curnode.left.data.get(0) + " A " + curnode.right.data.get(0));
//			}
			return curnode;
		} else { // this could potentially be a function. I'm gonna split it into 2 scenarios to take account of funcitons
			// cur problem: app of \x is getting assigned func
			int parnum = 0;
			ArrayList<String> lst1 = new ArrayList<String>(getList(tokens)); // maybe what you should do is check for the thing that goes in if it has the app w \x
			if (tokens.get(0).equals("(")) {
				parnum += 2;
			}
			ArrayList<String> lst2 = getList(new ArrayList<String>(tokens.subList(lst1.size() + parnum, tokens.size()))); // PROBLEM?
			// in the testing case, r gets separated from m. idk if that's the problem. Can experiment.
			if (tokens.get(lst1.size() + parnum).equals("(")) {
				parnum += 2;
			}
			//System.out.println("list 1: " + lst1 + " list 2: " + lst2);
			Node node2 = new Node(parse(lst1), parse(lst2));
			node2.ogToks.addAll(realToks);
			node2.type = "A";
			ArrayList<String> lst3 = new ArrayList<String>(tokens.subList(lst1.size() + lst2.size() + parnum, tokens.size()));
			//System.out.println("list 3: " + lst3);
			curnode = new Node (node2, parse(lst3));
			curnode.ogToks.addAll(realToks);
			if (lst3.get(0).equals(".")) { // THIS MAY BE RIGHT ?
				curnode.type = "F";
			} else {
				curnode.type = "A";
			}
			return curnode;
		}
	}
	
	//int spec = 0;
	public ArrayList<String> toString12 (Node curnode, ArrayList<String> retval) {
		System.out.println("?");
		if (curnode.data != null) {
			System.out.println(curnode.data);
		}
		if (curnode.type.equals("F") && spec == 0) {
//			retval.add("func");
			spec = 1;
			retval.add("(");
			if (curnode.left == null && curnode.right == null) {
				System.out.println("stuck1");
				retval.addAll(curnode.data);
				return retval;
			} if (curnode.left != null) {
				retval.addAll(toString2 (curnode.left, retval));
				System.out.println("stuck2");
			} if (curnode.right != null) {
//				retval.addAll(curnode.left.data); // this is going to be the period 
//				retval.add("(");
				retval.addAll(toString2 (curnode.right, retval)); // MIKA! bring commented back code and make toString2(curnode.right) and get rid of stuff in the bottom with 
				// variable if you fail the failure cases.
				System.out.println("stuck3");
				break;
				//retval.add(")");
			}
			//retval.add(")");
			spec = 0;
		} 
		
		else if (spec == 1) { // it's own version of things
//			if (curnode.type.equals("A")) {
//				retval.add("app");
//			} if (curnode.type.equals("F")) {
//				retval.add("func");
//			}
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				if (curnode.data.get(0).equals(".")) {
					retval.add("(");
					System.out.println("stuck4");
				}
				return retval;
			} if (curnode.left != null) {
				retval.addAll(toString2 (curnode.left, retval));
				System.out.println("stuck5");
			} if (curnode.right != null) {
				retval.addAll(toString2 (curnode.right, retval));
				System.out.println("stuck6");
			}
		} else {
			if (curnode.type.equals("A")) {
				retval.add("(");
				
//				retval.add("app");
			} 
//			if (curnode.type.equals("F")) {
//				retval.add("func");
//			}
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				return retval;
			} if (curnode.left != null) {
				retval.addAll(toString2 (curnode.left, retval));
				System.out.println("stuck7");
			} if (curnode.right != null) {
				retval.addAll(toString2 (curnode.right, retval));
				System.out.println("stuck8");
				retval.add(")");
			}
		}
		return retval;
	}
	
	ArrayList<String> retval = new ArrayList<String>();
	int spec = 0;
	public void toString2 (Node curnode) {
		if (curnode.type.equals("F") && spec == 0) {
			spec = 1;
			retval.add("(");
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				return;
			} if (curnode.left != null) {
				toString2 (curnode.left);
			} if (curnode.right != null) {
				curnode = curnode.right;
				retval.addAll(curnode.left.data); // this is going to be the period
				retval.add("(");
				toString2(curnode.right);
				retval.add(")");
			}
			retval.add(")");
			spec = 0;
		} 
		else if (spec == 1) { // it's own version of things
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				return;
			} if (curnode.left != null) {
				toString2 (curnode.left);
			} if (curnode.right != null) {
				toString2 (curnode.right);
			}
		} else {
			if (curnode.type.equals("A")) {
				retval.add("(");
			}
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				return;
			} if (curnode.left != null) {
				toString2 (curnode.left);
			} if (curnode.right != null) {
				toString2 (curnode.right);
				retval.add(")");
			}
		}
	}
	
	public void toString10 (Node curnode) {
		if (curnode.type.equals("F") && spec == 0) {
			retval.add("func");
			spec = 1;
			//retval.add("(");
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				return;
			} if (curnode.left != null) {
				toString10 (curnode.left);
			} if (curnode.right != null) {
//				retval.addAll(curnode.left.data); // this is going to be the period 
//				retval.add("(");
				toString10(curnode.right); // MIKA! bring commented back code and make toString2(curnode.right) and get rid of stuff in the bottom with 
				// variable if you fail the failure cases.
				//retval.add(")");
			}
			//retval.add(")");
			spec = 0;
		} 
		else if (spec == 1) { // it's own version of things
			if (curnode.type.equals("A")) {
				retval.add("app");
			} if (curnode.type.equals("F")) {
				retval.add("func");
			}
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				if (curnode.data.get(0).equals(".")) {
					//retval.add("(");
				}
				return;
			} if (curnode.left != null) {
				toString10 (curnode.left);
			} if (curnode.right != null) {
				toString10 (curnode.right);
			}
		} else {
			if (curnode.type.equals("A")) {
				//retval.add("(");
				retval.add("app");
			} if (curnode.type.equals("F")) {
				retval.add("func");
			}
			if (curnode.left == null && curnode.right == null) {
				retval.addAll(curnode.data);
				return;
			} if (curnode.left != null) {
				toString10 (curnode.left);
			} if (curnode.right != null) {
				toString10 (curnode.right);
				//retval.add(")");
			}
		}
	}
	
	public String toString (Node curnode) { // THIS METHOD IS SOMEHOW MESSING ME UP??
		int o = 0;
		int c = 0;
		String fn = "";
		toString2(curnode);
		//System.out.println(retval.size());
		for (int i = 0; i < retval.size(); i++) {
			fn += retval.get(i) + " ";
			if (retval.get(i).equals("(")) {
				o++;
			} if (retval.get(i).equals(")")) {
				c++;
			}
		}
		if (o > c) {
			while (o> c) {
				retval.add(")");
				fn += (") ");
				c++;
			}
		}
		retval = new ArrayList<String>();
		return fn.substring(0, fn.length() - 1); // - 1 to get rid of extra space
	}
	
	public String toStringTesting (Node curnode) { // THIS METHOD IS SOMEHOW MESSING ME UP??
		int o = 0;
		int c = 0;
		toString10(curnode);
		String fn = "";
		for (int i = 0; i < retval.size(); i++) {
			fn += retval.get(i) + " ";
			if (retval.get(i).equals("(")) {
				o++;
			} if (retval.get(i).equals(")")) {
				c++;
			}
		}
		if (o > c) {
			while (o> c) {
				retval.add(")");
				fn += (") ");
				c++;
			}
		}
		retval = new ArrayList<String>();
		return fn.substring(0, fn.length() - 1); // - 1 to get rid of extra space
	}
	
	public ArrayList<String> fixThings (Node curnode) {
		Parser parser = new Parser();
		int o = 0;
		int c = 0;
		toString2(curnode);
		String fn = "";
		for (int i = 0; i < retval.size(); i++) {
			fn += retval.get(i) + " ";
			if (retval.get(i).equals("(")) {
				o++;
			} if (retval.get(i).equals(")")) {
				c++;
			}
		}
		if (o > c) {
			while (o> c) {
				retval.add(")");
				fn += (") ");
				c++;
			}
		}
		ArrayList<String> ogToks = retval;
		ogToks = parser.preParse(ogToks);
		retval = new ArrayList<String>();
		return ogToks;
	}
	
	public String toString6 (Node curnode) {
		ArrayList<String> done = curnode.ogToks;
			String fn = "";
			for (int i = 0; i < done.size(); i++) {
				fn += done.get(i) + " ";
			}
			return fn.substring(0, fn.length() - 1); // - 1 to get rid of extra space
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
					n = i+1;
					int o = 1;
					int c = 0;
					while (n < tokens.size() && c < o) {
//						if (tokens.get(n).equals("(") && tokens.get(n-1).equals(".")) {
//							// meaning parens are part of it ig
//						}
						if (tokens.get(n).equals("(")) {
							o++;
						} else if (tokens.get(n).equals(")")) {
							c++;
						}
						n++;
					}
					tokens.add(n, ")");
					// i + 4 in this scenario would get us to the space after the dot of this lambda
					if (n == tokens.size()-1 && tokens.get(i + 4).equals("(")) { // this means a variable got captured that won't be considered in calculations normally
						tokens.add(i+4, "(");
						tokens.add(tokens.size()-1, ")");
					}
				} // run (\m.\r.(\b.\c.\a.\r.b c a r) m r)
			}
		}
		//System.out.println(tokens.toString());
		// eat up things to the right
		for (int i = 0; i < tokens.size(); i++) { // go back to this... a little messed up :( figure out multi paren rule
			// go back to the parenthesis if time.. just placed weirdly but shouldn't affect functionality
			if (tokens.get(i).equals(".")) {
				if (i == 0 || !tokens.get(i+1).equals("(")) {
					tokens.add(i+1, "(");
					n = i+2;
					int o = 1;
					int c = 0;
					int v = 0;
					int ind = 0;
					while (n < tokens.size() && c < o) {
						//System.out.println(tokens.get(n));
						if (tokens.get(n).equals("(")) {
							o++;
							v = 0;
							ind = 0;
						}
						else if (tokens.get(n).equals(")") && c+1 < o) {
							c++;
							tokens.add(i+1, "(");
							tokens.add(n+1, ")");
							n+= 2;
							v = 0;
							ind = 0;
						} else if (tokens.get(n).equals(")")) {
							c++;
							v = 0;
							ind = 0;
						} 
						else if (v == 2){
							tokens.add(ind, "(");
							tokens.add(n+1, ")");
							v = 0; // probs gonna look horrible at first.. check it out
							ind = 0;
							n+=2;
						}
						else {
							if (ind == 0) {
								ind = n;
							}
							v++;
						}
						n++;
					}
					tokens.add(n, ")");
				} else {
					n = i+2;
					int o = 1;
					int c = 0;
					int add = 0;
					while (n < tokens.size() && c < o) {
						//System.out.println(tokens.get(n));
						if (tokens.get(n).equals("(")) {
							o++;
						} else if (tokens.get(n).equals(")")) {
							c++;
						}
						if (c == o && tokens.get(n+1).equals("(")) {
							tokens.add(i+1, "(");
							n += 2;
							o++;
							add++;
						}
						n++;
				}
					while (add > 0) {
						tokens.add(n+1, ")");
						add--;
					} // this whole else part i added might be very very wrong. I need a charger.
			}
			}
		}
		return tokens;
			}
}
