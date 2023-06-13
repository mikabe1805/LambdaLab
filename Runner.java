import java.util.ArrayList;
import java.util.StringUtils;

import Parser.Node;


public class Runner {
	// CURRENT PROBLEM: APP FINDER
	public static Parser.Node run (Parser.Node n) {
		// run (\m.(\n.(\f.(\x.((m f) ((n f) x)))))) (\f. (\x. (f (f x)))) (\f. (\x. (f x)))
		Parser parser = new Parser();
//		System.out.println(parser.toString(n));
		//Parser.Node temp = n;
		//System.out.println(isRunnable(n));
		//System.out.println(parser.toString(n));
		if (n.type.equals("V")) { // variable
			return n;
		} else if (!n.ogToks.contains("\\")) {
			return n;
		} else if (!isRunnable(n)) { // single function with no application if F is top of stack // POSSIBLY CULPRIT
			return n;
		} else { // make sure func finder operates properly
			//Parser.Node func = funcfinder(n);
			//Parser parser = new Parser(); // for testing
			//System.out.println(parser.toString(n)); // run \a. (\b. \a. b a) a
			// run (\m.\r.(\b.\c.\a.\r.b c a r) m r) a r
			Parser.Node app = appfinder(n); // POSSIBLE CULPRIT
			//System.out.println(parser.toString(n));
			//System.out.println("curnode " + parser.toString(n));
			ArrayList<String> guide = appfinder2(n, new ArrayList<String>()); // POSSIBLE CULPRIT
			//System.out.println(parser.toString(n));
			// this is basically our guide home so we're able to replace the new app
			// once we get to the last guide letter, that's the final curnode. guide letter = new func
			// take into account case w no instruction (a (\x. x b))
			Parser.Node insert = new Parser.Node(null);
			Parser.Node func = new Parser.Node(null);
				if (app.left.type.equals("F")) {
					func = app.left;
					insert = app.right; // what to put into the function will be on the right (also default will be right function goes into left function
				} else {
					func = app.right;
					insert = app.left; // what to put into the function will be on the left
				}
				//System.out.println(parser.toString(insert));
//				System.out.println(guide);
				String varname = func.left.right.data.get(0); // gets var name, since function has subcategory app on left for \ and var. var is on the right.
				Parser.Node replace = func.right.right; // got rid of the dot because it was annoying me
//				System.out.println("replace " + parser.toString(replace));
//				System.out.println("replace with " + parser.toString(insert));
				Parser.Node newApp = varreplacer(replace, varname, insert); // PROBLEM: chopped off \n
//				System.out.println("still alive");
//				System.out.println("new app " + parser.toString(newApp));
//				System.out.println(guide.toString());
//				System.out.println(parser.toString(n));
				//System.out.println(parser.toString(newApp));
				// now's the part to remake the function..
				// next thing: call on fixer to fix the og toks
				Parser.Node finall = new Parser.Node(null);
				
				if (guide.size() == 0 || guide.contains("M")) { // the node is only the application for function
					// if it contains M, that means that it's found right away
					finall= newApp;
				} else {
					//System.out.println(n);
					finall= finalNode(n, newApp, guide); // problem ??
				}
//				System.out.println("still here");
//				System.out.println("new app: " + parser.toString(newApp));
//				System.out.println("final: " + parser.toString(finall)); // FINAL IS THE ISSUE ! MAYVE??
				finall.backup = parser.toString(finall);
				ArrayList<String> fixedToks = parser.fixThings(finall);
				finall = fix(finall, fixedToks);
				//System.out.println("done");
				return finall;
				}
	}
		
	public static Parser.Node funcfinder(Parser.Node n) {
		Parser.Node t = new Parser.Node(null);
		if (n.type.equals("F")) {
			return n;
		} else {
			if (n.left != null) {
				t = funcfinder(n.left);
			}
			if (n.right != null && !t.type.equals("F")) {
				t = funcfinder(n.right);
			}
		}
		if (t.type.equals("F")) {
			return t;
		} else {
			return n;
		}
	}
	public static Parser.Node appfinder6(Parser.Node n) {
		Parser parser = new Parser();
		Parser.Node t = new Parser.Node(null);
		//System.out.println(parser.toString(n));
		if (n.left != null) { 
			if (n.left.type.equals("F")) {
				if (n.right.data != null && n.right.data.get(0).equals(".")) {
					
				} else {
					return n;
				}
			}
		} if (n.right != null) {
			if (n.right.type.equals("F")) {
				if (n.left.data != null && n.left.data.get(0).equals(".")) {
					
				} else {
					return n;
				}
			}
		}
		if (n.left != null) { // repetitive, rework later
			t = appfinder(n.left);
			if (t.left != null) {
				if (t.left.type.equals("F")) {
					if (t.right.data != null && t.right.data.get(0).equals(".")) {
						
					} else {
						n = t;
					}
				}
			}
			if (t.right != null) {
				if (t.right.type.equals("F")) {
					if (t.left.data != null && t.left.data.get(0).equals(".")) {
						
					} else {
						n = t;
					}
				}
			}
		}
		// (n.right != null && !(n.left.type.equals("F") && !n.right.data.get(0).equals(".")) && !(n.right.type.equals("F") && !n.left.data.get(0).equals(".")))
		if (n.right != null) {
			if ((!n.right.type.equals("F") || (n.left.data != null && n.left.data.get(0).equals("."))) && (!n.left.type.equals("F") || (n.right.data != null && n.right.data.get(0).equals(".")))) {
				t = appfinder(n.right);
					if (t.left != null) {
						if (t.left.type.equals("F")) {
							if (t.right.data != null && t.right.data.get(0).equals(".")) {
								
							} else {
								n = t;
							}
						}
					}
					if (t.right != null) {
						if (t.right.type.equals("F")) {
							if (t.left.data != null && t.left.data.get(0).equals(".")) {
								
							} else {
								n = t;
							}
						}
					}
				}
//			if (!n.left.type.equals("F") || (n.right.data != null && n.right.data.get(0).equals("."))) {
//				t = appfinder(n.right);
//					if (t.left != null) {
//						if (t.left.type.equals("F")) {
//							if (t.right.data != null && t.right.data.get(0).equals(".")) {
//								
//							} else {
//								n = t;
//							}
//						}
//					}
//					if (t.right != null) {
//						if (t.right.type.equals("F")) {
//							if (t.left.data != null && t.left.data.get(0).equals(".")) {
//								
//							} else {
//								n = t;
//							}
//						}
//					}
//			}
		}
		return n;
		}
	
	public static Parser.Node appfinder(Parser.Node n) {
		Parser parser = new Parser();
		Parser.Node temp = new Parser.Node(null);
		Parser.Node t = new Parser.Node(null);
		t.type = "M";
		//System.out.println(parser.toString(n));
		if (n.left != null) { 
			if (n.left.type.equals("F")) {
				if (n.right.data != null && n.right.data.get(0).equals(".")) {
					
				} else {
					return n;
				}
			}
			temp = appfinder(n.left);
			if (!temp.type.equals("M")) {
				return temp;
			}
		} if (n.right != null) {
			if (n.right.type.equals("F")) {
				if (n.left.data != null && n.left.data.get(0).equals(".")) {
					
				} else {
					return n;
				}
			}
			temp = appfinder(n.right);
			if (!temp.type.equals("M")) {
				return temp;
			}
		}
		return t;
		}
	
	public static boolean contains (Parser.Node n, String varname) {
		boolean t = false;
		if (n.data != null) {
			if (n.data.get(0).equals(varname)) {
				return true;
			}
		}
		if (n.left != null) {
			t = contains(n.left, varname);
		}
		if (n.right != null && !t) {
			t = contains(n.right, varname);
		}
		return t;
	}
	
	public static Parser.Node fix (Parser.Node n, ArrayList<String> ogToks) {
		n.ogToks = ogToks;
		if (n.left == null && n.right == null) {
			return n;
		}
		if (n.left != null) {
			n.left = fix(n.left, ogToks);
		}
		if (n.right != null) {
			n.right = fix(n.right, ogToks);
		}
		return n;
	}
	
	public static boolean isRunnable (Parser.Node n) {
		//System.out.println(n.type); // problem: should be app
		Parser parser = new Parser(); // for testing
		if (n.type.equals("V")) {
			return false;
		}
		if (n.type.equals("A")) { // this is checking to see if the method is already in a runnable composition
			if ((n.right.type.equals("F") && ((n.left.data != null && !n.left.data.get(0).equals("."))) || n.left.data == null) || (n.left.type.equals("F") && ((n.right.data != null && !n.right.data.get(0).equals("."))) || (n.right.data == null))) {
				return true;
			}
		}
		// if the top one is not immediately runnable, check to see if it's runnable at all
		//System.out.println("it gets here");
		
		Parser.Node og = n;
		String type = n.type;
		Parser.Node third = appfinder(og);
		//System.out.println("done");
		//System.out.println(parser.toString(third));
		if (!third.type.equals("M")) { // this would mean that it couldn't find a function and returned the same thing (i think)
			og.type = type;
			return true;
		}
		og.type = type;
		return false;
		
	}
	
	public static ArrayList<String> appfinder3(Parser.Node n, ArrayList<String> m) { // DRAFT 1!
		ArrayList<String> may = new ArrayList<>();
		ArrayList<String> fin = new ArrayList<>();
		Parser.Node t = new Parser.Node(null);
		if (n.left != null) { 
			if (n.left.type.equals("F")) {
				//fin.add("L");
				return fin;
			}
		} if (n.right != null) {
			if (n.right.type.equals("F")) {
				//fin.add("R");
				return fin;
			}
		}
		if (n.left != null) { // repetitive, rework later
			t = appfinder(n.left);
			may = appfinder2(n.left, fin);
			if (t.left != null) {
				if (t.left.type.equals("F")) {
					fin.add("L");
					fin.addAll(may);
					n = t;
				}
			}
			if (t.right != null) {
				if (t.right.type.equals("F")) {
					fin.add("L");
					fin.addAll(may);
					n = t;
				}
			}
		}
		if (n.right != null && !n.left.type.equals("F") && !n.right.type.equals("F")) {
			t = appfinder(n.right);
			may = appfinder2(n.right, fin);
			if (t.left != null) {
				if (t.left.type.equals("F")) {
					fin.add("R");
					fin.addAll(may);
					n = t;
				}
			}
			if (t.right != null) {
				if (t.right.type.equals("F")) {
					fin.add("R");
					fin.addAll(may);
					n = t;
				}
			}
		}
		return fin;
		}
	
	public static ArrayList<String> appfinder2(Parser.Node n, ArrayList<String> m) { // for replacement purposes.. messy but effective
		//System.out.println(m);
		// check out where the apps are
		ArrayList<String> may = new ArrayList<>();
		ArrayList<String> fin = new ArrayList<>();
		if (n.left != null) { 
			if (n.left.type.equals("F")) {
				if (n.right.data != null && n.right.data.get(0).equals(".")) {
					
				} else {
					fin.add("M");
					return fin;
				}
			}
			may = appfinder2(n.left, fin);
			if (!may.isEmpty()) {
				if (may.contains("M")) {
					may.remove("M");
				}
				fin.addAll(may);
				fin.add("L");
				return fin;
			}
		} 
		if (n.right != null) {
			if (n.right.type.equals("F")) {
				if (n.left.data != null && n.left.data.get(0).equals(".")) {
					
				} else {
					fin.add("M");
					return fin;
				}
			}
			may = appfinder2(n.right, fin); //
			if (!may.isEmpty()) {
				if (may.contains("M")) {
					may.remove("M");
				}
				fin.add("R");
				fin.addAll(may);
				return fin;
			}
		}
		return fin;
		}
	
	public static ArrayList<String> funcfinder2(Parser.Node n, ArrayList<String> m) { // for replacement purposes.. messy but effective
		//System.out.println(m);
		// check out where the apps are
		ArrayList<String> may = new ArrayList<>();
		ArrayList<String> fin = new ArrayList<>();
		if (n.left != null) { 
			if (n.left.type.equals("F")) {
					fin.add("M");
					return fin;
			}
			may = appfinder2(n.left, fin);
			if (!may.isEmpty()) {
				if (may.contains("M")) {
					may.remove("M");
				}
				fin.addAll(may);
				fin.add("L");
				return fin;
			}
		} 
		if (n.right != null) {
			if (n.right.type.equals("F")) {
					fin.add("M");
					return fin;
			}
			may = appfinder2(n.right, fin); //
			if (!may.isEmpty()) {
				if (may.contains("M")) {
					may.remove("M");
				}
				fin.add("R");
				fin.addAll(may);
				return fin;
			}
		}
		return fin;
		}
	
	
	public static Parser.Node varreplacer (Parser.Node n, String varname, Parser.Node rw) { // rw stands for replace with
		Parser parser = new Parser();
		Parser.Node decoy = new Parser.Node(null);
		decoy.type = "M";
//		System.out.println("gets here with2 " + parser.toString(n) + "replace with: " + parser.toString(rw) + "var name: " + varname);
		// nvm im stupid.
		if (n.type.equals("F") && n.left.right.data.get(0).equals(varname)) { 
			return decoy;
		}
//		if (n.type.equals("F") && contains(rw, n.left.right.data.get(0)) && !rw.type.equals("F")) {
		if (n.type.equals("F") && (rw.type.equals("V") && rw.data.get(0).equals(n.left.right.data.get(0)))) {
			//System.out.println("gets here with " + parser.toString(n) + "replace with: " + parser.toString(rw) + "var name: " + varname);
			String newvarname = changedName(n);
			ArrayList<String> data = new ArrayList<>();
			data.add(newvarname);
			Parser.Node replace = new Parser.Node(data);
			n.right = varreplacer(n.right, n.left.right.data.get(0), replace);
			n.left.right = replace;
		}
		if (n.type.equals("V")) {
			if (n.data.get(0).equals(varname)) {
//				System.out.println("replaced!");
				n = rw;
			}
			return n;
		}
		if (n.left != null) {
			decoy = varreplacer(n.left, varname, rw);
			if (decoy.type.equals("M")) {
				ArrayList<String> guide2 = funcfinder2(n.left.right, new ArrayList<String>());
				System.out.println(guide2);
				if (guide2.size() != 0) {
					while (guide2.size() > 1) {
						if (guide2.get(0).equals("R")) {
							n = n.right;
						} else {
							n = n.left;
						}
						guide2 = new ArrayList<String>(guide2.subList(1, guide2.size()));
					} 
					if (guide2.get(0).equals("R")) {
						n.right = varreplacer(n.right, varname, rw);
					} else {
						n.left = varreplacer(n.left, varname, rw);
					}
				} else {
					return n;
				}
			} else {
				n.left = decoy;
			}
		}
		if (n.right != null) {
			decoy = varreplacer(n.right, varname, rw);
			if (decoy.type.equals("M")) {
				ArrayList<String> guide2 = funcfinder2(n.right.right, new ArrayList<String>());
				System.out.println(guide2);
				if (guide2.size() != 0) {
					while (guide2.size() > 1) {
						if (guide2.get(0).equals("R")) {
							n = n.right;
						} else {
							n = n.left;
						}
						guide2 = new ArrayList<String>(guide2.subList(1, guide2.size()));
					} 
					if (guide2.get(0).equals("R")) {
						n.right = varreplacer(n.right, varname, rw);
					} else {
						n.left = varreplacer(n.left, varname, rw);
					}
				} else {
					return n;
				}
			} else {
				n.right = decoy;
			}
		}
		return n;
	}
	
	public static String changedName (Parser.Node n) { // this method will make a new var name and then put it into varreplacer to change it all
		String varname = n.left.right.data.get(0); // to get og var name
		if (varname.length() == 1) {
			varname += "1";
		} else if (isNumeric(varname.substring(varname.length()-1, varname.length()))) {
			int intValue = Integer.parseInt(varname.substring(varname.length()-1, varname.length())) + 1;
			varname = varname.substring(0, varname.length()-1);
			varname += intValue;
		} else {
			varname += "1";
		}
		while (contains(n, varname)) {
			varname = changedName(n);
		}
		return varname;
		
	}
	private static boolean isNumeric(String string) {
	    int intValue;
		try {
	        intValue = Integer.parseInt(string);
	        return true;
	    } catch (NumberFormatException e) {
	    	
	    }
	    return false;
	}
	public static Parser.Node finalNode (Parser.Node og, Parser.Node neww, ArrayList<String> guide) { // PROBLEM !!
		// ok i was on to nothing
		Parser parser = new Parser();
		//System.out.println(guide);
		// 0 = \f.\x.x
		//succ = \n.\f.\x.f(n f x)
		// 1 = run succ 0
		//+ = \m.\n.\f.\x.(m f) ((n f) x)
		// 2 = run succ 1
		// 3 = run + 2 1
		// run ( \ m . ( \ n . ( \ f . ( \ x . ( m f ) ( ( n f ) x ) ) ) ) ) ( \ f . ( \ x . ( f f x ) ) ) ( \ f1 . ( \ x1 . ( f1 x1 ) ) )
		// * = \n.\m.\f.\x.n (m f) x
		// 4 = run * 2 2
		// run ( \ n . ( \ m . ( \ f . ( \ x . ( ( n ( m f ) ) x ) ) ) ) ) ( \ f . ( \ x . ( f f x ) ) ) ( \ f . ( \ x . ( f f x ) ) )
//		System.out.println("in finalnode " + parser.toString(og)); // hey mika! og is really fucked up for some reason. check it out.
		if (guide.size() == 1) {
			if (guide.get(0).equals("L")) {
				og.left = neww;
				return og;
			} else {
				og.right = neww;
				return og;
			}
		}
		if (guide.get(0).equals("R")) {
			og.right = finalNode(og.right, neww, new ArrayList<String>(guide.subList(1, guide.size())));
			return og;
		} else {
			og.left = finalNode(og.left, neww, new ArrayList<String>(guide.subList(1, guide.size())));
			return og;
		}
	}
	
}


