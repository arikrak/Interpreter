public class Parse {

	Splitter split;
	Token tok;
	String text; // token string. use
	int val; // token value
	Node root;

	final int INTEGER = 257;
	final int DOUBLE = 258;
	final int IDENT = 260;
	final int CONSTH = 261;

	public Parse(Splitter spl) {
		split = spl;
		program();
	}

	public void program() {
		root = new Node(1, null, 1, "root");
		//System.out.println("in program: " + text);
		lex(); // initial lex
		if (text.equals("int") || text.equals("char")) { // checks if listOfDec
			listOfDec(root);
		}
		listOfStat(root);
	}

	private void listOfDec(Node p) {
		//System.out.println("in listofdec: " + text);
		Node n = addNode(p, 10, "listOfDec" );
		declaration(n);
		if (text.equals("int") || text.equals("char")) { // checks if listOfDec
			listOfDec(n);
		}

	}

	private void declaration(Node p) {
		//System.out.println("in dec: " + text);
		Node n = addNode(p, 20, "declaration");
		type(n);

		listOfVariables(n);

		if (text.equals(";")) {
			//System.out.println("finished declaration");
			lex();
		} else {
			//error();
		}

	}

	
	private void type(Node p) {
		//System.out.println("in type: " + text);
		Node n = addNode(p, 30, "type");
		// <type> --> "int" | “char”
		if (text.equals("int")) {
			//System.out.println("reached int keyword: " + text);
			addLeaf(n);
			lex(); // end
		} else if (text.equals("char")) {
			//System.out.println("reached char keyword: " + text);
			addLeaf(n);
			lex();
		} else {
			error();
		}

	}

	private void listOfVariables(Node p) {
		//System.out.println("in listOfVariables : " + text);
		Node n = addNode(p, 40, "listOfVariables");
		variable(n);

		if (text.equals(",")) {
			lex();
			listOfVariables(n);
		}

	}

	private void variable(Node p) {
		//System.out.println("in var : " + text);
		Node n = addNode(p, 50, "variable");
		if (val == (IDENT)) {
			//System.out.println("reached variable: " + text);
			addLeaf(n);
			lex(); // end..
		}
	}



//<listofstatements --> <statement> “;” <listofstatements>
private void listOfStat(Node p) {	
	
	if (val == 500) {
		//System.out.println ("finshed listOfStat and all tokens");
	}
	
	else{	
		//System.out.println("in listOfstat " + text);
		Node n = addNode(p, 60, "listOfStat");
		statement(n);

		if (text.equals(";")) {
			lex();			
			listOfStat(n);			
		}		
	}
	
		
	}



	private void statement(Node p) {
		//System.out.println("in statement " + text);
		Node n = addNode(p, 70, "statement");
		// <statement> --> <cin> | <cout> | <if> | <assign> | <while> | “{“
		// <listofstatements> “}”

		int curTok = val;

		switch (curTok) {
		case 0:
			cin(n);
			break;
		case 1:
			cout(n);
			break;
		case 2:
			ifStat(n);
			break;
		case 3:
			whileStat(n);
			break;
		default:
			if (text.equals("{")) {
				lex();
				listOfStat(n);

				if (text.equals("}")) {

					//System.out.println("finished statement");
					lex();
				} else {
					error();
				}
			} else {
				assignStat(n);
			}
		}
	}

	private void whileStat(Node p) {
		// need else by everyone to call error()
		//System.out.println("whileStat");
		Node n = addNode(p, 80, "whileStat");

		// less messy
		// if(ifLex("while")&&ifLex("(")){
		// exp(n);
		// if(ifLex(")")){
		// statement(n);
		// }
		// }

		if (text.equals("while")) {
			lex();
			
			//extra node for before expression (can also go directly to it)
			Node whileSpot = new Node(902,text,902,"jumpToHereAfterWhile");
			n.addNode(whileSpot);
			
			
			if (text.equals("(")) {
				lex();
				exp(n);

				if (text.equals(")")) {
					lex();
					
					//addjumpfalse node
					Node jumpFalse = new Node(900, text, 900, "jumpAwayFromHereIfFalse");
					n.addNode(jumpFalse);					
					
					statement(n);
					
					//add jump to exp node
					Node jump = new Node(905, text, 905, "jumpToExpression");
					n.addNode(jump);	
					
				}
			}
		}

	}

	private void assignStat(Node p) {
		// <assign> --> <listofvariables> "=" <exp>
		//System.out.println("assignStat");
		Node n = addNode(p, 90, "assignStat");
		listOfVariables(n);

		if (text.equals("=")) {
			lex();
			exp(n);
		} else {
			error();
		}
	}

	private void ifStat(Node p) {
		// <if> --> “if” “(" <exp> ")" <statement> ["else" <statement>]'
		Node n = addNode(p, 100, "ifStat");
		//System.out.println("ifStat");

		// can use ifLex();
		// if(ifLex("if")&&ifLex("(")){
		// exp(n);
		// }
		// if(ifLex(")")){
		// statement(n);
		// }
		// if(ifLex("else")){
		// statement(n);
		// }

		if (text.equals("if")) {
			lex();
			if (text.equals("(")) {
				lex();
				exp(n);

				if (text.equals(")")) {
					
					//add special child for quads to get point **
					Node jumpFalse = new Node(900, text, 900, "jumpAwayFromHereIfFalse");
					n.addNode(jumpFalse);					
					
					lex();
					statement(n);
					
					//System.out.println ("after stmt:"+text);
					
					//if there's a ";" will need another lex
					if(text.equals(";")){
						lex(); }

					if (text.equals("else")) {					
						lex();	
						
						////System.out.println ("lex'd else"+text);
						
						Node leaf = new Node(val, text, 901, "else"); 
						n.addNode(leaf);						
						
						statement(n);
						
						//add special child for quads to get point **
						//not needed?
						Node elseSpot = new Node(902, text, 902, "endElse"); 
						n.addNode(elseSpot);	
					}
				}
			}
		}
	}

	private void cout(Node p) {
		// <cout> --> "cout" "<<" <listofexpressions>
		Node n = addNode(p, 110, "cout");
		// if(ifLex("cout")&&ifLex("<<")){
		// listOfExpressions(n);
		// }
		if (text.equals("cout")) {
			lex();
			if (text.equals("<<")) {
				lex(); // need here
				listOfExpressions(n);
			}
		}
	}

	private void cin(Node p) {
		// "cin" ">>" <listofvariables>
		Node n = addNode(p, 120, "cin");
		if (text.equals("cin")) {
			lex();
			if (text.equals(">>")) {
				lex();
				listOfVariables(n);
			}
		}
		// if(ifLex("cin")&&ifLex(">>")){
		// listOfVariables(n);
		// }
	}

	// <listofexpressions>--><exp> |<exp>“,”<listofexpressions>
	private void listOfExpressions(Node p) {
		Node n = addNode(p, 130, "listOfExpressions");
		exp(n);
		if (ifLex(",")) {
			listOfExpressions(n);
		}

	}

	private void exp(Node p) {
		// <exp> --> <exp1> { “||” <exp1>}*
		//System.out.println("in exp");
		Node n = addNode(p, 140, "exp");

		exp1(n);
		while (text.equals("||")) {
			 //directly add Node since not continuing with child
			Node leaf = new Node(val, text, 490, text); //leaf num
			n.addNode(leaf);

			lex();
			exp1(n);
		} // always ends with lex(); so next thing after it doesn't need it.
	}

	private void exp1(Node p) {
		Node n = addNode(p, 150, "exp1");
		exp2(n);

		while (text.equals("&&")) {
			Node leaf = new Node(val, text, 491, text);
			n.addNode(leaf);

			lex();
			exp2(n);
		}
	}


	//note** may need to mark these stuff instead of just lexing them?


	// <exp2> --> “!” <exp2> | <exp3> { “|” <exp3>}*
	private void exp2(Node p) {
		Node n = addNode(p, 160, "exp2");
		if (text.equals("!")) {
			Node leaf = new Node(val, text, 492, text);
			n.addNode(leaf);

			lex();
			exp2(n);

		} else {
			exp3(n);

			while (text.equals("|")) {
				Node leaf = new Node(val, text, 493, text);
				n.addNode(leaf);

				lex();
				exp3(n);

			}

		}
	}

	private void exp3(Node p) {
		Node n = addNode(p, 170, "exp3");
		exp4(n);
		while (text.equals("^")) {
			Node leaf = new Node(val, text, 494, text);
			n.addNode(leaf);

			lex();
			exp4(n);
		}

	}

	private void exp4(Node p) {
		Node n = addNode(p, 180, "exp4");
		exp5(n);
		while (text.equals("&")) {
			Node leaf = new Node(val, text, 495, text);
			n.addNode(leaf);

			lex();
			exp5(n);
		}

	}

	private void exp5(Node p) {
		Node n = addNode(p, 190, "exp5");
		if (text.equals("~")) {
			Node leaf = new Node(val, text, 496, text);
			n.addNode(leaf);

			lex();
			exp5(n);
		} else {
			exp6(n);
			while (text.equals("==") || text.equals("!=")) { // mark which*
				Node leaf = new Node(val, text, 497, text);
				n.addNode(leaf);

				lex();
				exp6(n);
			}

		}
	}

	// <exp6> --> <exp7> { <relop> <exp?>}*
	private void exp6(Node p) {
		Node n = addNode(p, 200, "exp6");
		exp7(n);
		// run it and do nothing if it does nothing..

		while (relop(n)) {
			lex();
			exp7(n);  //this doesn't seem to get called since it returns false ?**
		}

	}

	// <relop>--> “<=”|”<”|”>”|”>=”
	//very risky way.. 
	private boolean relop(Node p) {
		//System.out.println("CHECKING RELOP");
		// end  insert leaves
		// now adds specific for relop and gen for leaf*
		//System.out.println ("the relop: "+text + val);
		Node n;
		switch (val) {
		case 24:
			n = addNode(p, 410, "relop");
			addLeaf(n);
			return true;
		case 19:
			n = addNode(p, 411, "relop");
			addLeaf(n);
			////System.out.println("<"); 
			return true;
		case 26:
			n = addNode(p, 412, "relop");
			addLeaf(n);
			//System.out.println(">");
			return true;
		case 27:
			n = addNode(p, 413, "relop");
			addLeaf(n);
			return true;
		default:
			//System.out.println ("no relop");
			return false;
			//continue?
		}
		//return true;
	}

	private void exp7(Node p) {
		//System.out.println("EXP7");
		Node n = addNode(p, 220, "exp7");
		exp8(n);
		while (addop(n)) {
			lex();
			exp8(n);
		}

	}


	//mulop addaop relop, had tried to reduce code poorly
	//is there still a redundacy with addNode and addLeaf??***

	private boolean addop(Node p) {
		//System.out.println("CHECKING ADDOP");
		// end
		Node n;
		if (val == 29) {
			// +
			n = addNode(p, 430, "addop");
			addLeaf(n);
			return true;
		} else if (val == 30) {
			// -
			n = addNode(p, 431, "addop");
			addLeaf(n);
			return true;
		} else {
			return false;
		}
	}

	private void exp8(Node p) {
		//System.out.println("EXP8");
		Node n = addNode(p, 240, "exp8");
		exp9(n);
		while (mulop(n)) { //check to make sure its not calling each time**
			lex(); // can move below
			exp9(n);
		}

	}

	private boolean mulop(Node p) {
		// end**

		//put by each can't auto do***  check also relop etc.
		Node n;
		if (val == 31) {
			// *
			n = addNode(p, 450, "mulop");
			addLeaf(n);
			return true;
		} else if (val == 32) {
			// /
			n = addNode(p, 451, "mulop");
			addLeaf(n);
			return true;
		} else if (val == 33) {
			// %
			n = addNode(p, 452, "mulop");
			addLeaf(n);
			return true;
		} else {
			return false;
		}
	}


	// --> “(“ <exp> “)” |INTEGER|IDENT|CONSTCH | <uarop> <exp9>
	private void exp9(Node p) {
		//System.out.println("in exp9 " + text + " "+ val);
		Node n = addNode(p, 260, "exp9");

		// or select case..

		// end
		if (text.equals("(")) {
			lex();
			exp(n);
			if (text.equals(")")) {
				lex();
			}
		} else if (val == IDENT) {
			//System.out.println("IDENT:" + text);
			addLeaf(n);
			lex();

		} else if (val == INTEGER) {
			//System.out.println("INTEGER " + text+"."+val);
			addLeaf(n);
			lex();
		} else if (val == CONSTH) {
			//System.out.println("CONSTH");
			addLeaf(n);
			lex();
		} else {

			uarop(n);
			exp9(n);
		}

	}

	private void uarop(Node p) {
		//System.out.println("in uarop");
		

		// end
		if (text.equals("-")) {
			Node n = addNode(p, 270, "uarop");
			addLeaf(n);
			lex();
		} else if (text.equals("+")) {
			Node n = addNode(p, 271, "uarop");
			addLeaf(n);
			lex();
		} else {
			error();
			lex(); // (or die)
		}

	}


	/* General methods */

	public void lex() {
		try {
			tok = split.getToken();
			text = tok.getString();
			val = tok.getType();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void error() {
		System.err.println ("error: " + text + " " + val );
	}



	private Node addNode(Node parent, int method, String strMethod) {
		Node newNode = new Node(val, text, method, strMethod);
		parent.addNode(newNode);
		return newNode;
	}

	private void addLeaf(Node parent) {
		Node leaf = new Node(val, text, 0, text); //leaf num
		parent.addNode(leaf);
	}

	// run boolean check while lexing..
	private boolean ifLex(String s) {
		if (text.equals("s")) {
			lex();
			return true;
		} else {
			return false;
		}
	}


	//after tree created, can print it.
	private void printSubTree(Node n)
	{
		//now post order
		if(n.children!=null){
			for(Node child: n.children){
				printSubTree(child);
			}

		}
		//System.out.println ("");
		n.print();
	}

	public void printTree()
	{
		printSubTree(root);
	}

	public Node returnRoot()
	{
		return root;
	}




}
