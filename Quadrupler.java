import java.util.ArrayList;

public class Quadrupler {

	ArrayList<Quadruple> quads;

	int numInQuad;

	// changed everything to ints

	// take in root
	public Quadrupler(Node treeRoot) {

		quads = new ArrayList<Quadruple>();

		// xtraTreePrep(treeRoot);
		postOrder(treeRoot);

		for (Quadruple q : quads) {
			System.out.println("[op: " + q.oper + " vars:" + q.var1 + " "
					+ q.var2 + " result:" + q.result + "]");

		}

	}

	public void postOrder(Node nod) {
		// or select case
		int childnum = nod.children.size();

		if (childnum == 0) {

			// already connected temp to data.

			// jump quads :( :)
			if (nod.method == 900 || nod.method == 901 || nod.method == 902
					|| nod.method == 905) {
				Quadruple q = new Quadruple(nod.method, null, null, null);
				quads.add(q);
				nod.pointArray = q;
				numInQuad = quads.indexOf(q);
			}
			//cout
			else if(nod.method==110){
				Object word = nod.children.get(0).temp;
				makeQuad(nod.method, word, null, null, nod);
			}


		}

		else {
			// first do children
			for (Node child : nod.children) {
				postOrder(child);
			}

			if (childnum == 1) {
				nod.temp = nod.children.get(0).temp;
			}

			else if (childnum == 2) { // 2 and 3 separate for clarity
				nod.print();

				// System.out.println(" has 2 children");

				Node child = nod.children.get(0);
				int iop = child.method;
				// String sop = nod.children.get(0).strMethod;
				Object temp = genTemp();

				// unary ops
				// -
				// <exp2> --> “!” <exp2>
				// <exp5> --> “~ “<exp5>
				if (iop == 270 || iop == 271 || iop == 160 || iop == 190
						|| iop == 492 || iop == 496) {
					makeQuad(iop, nod.children.get(1).temp, null, temp, nod);
				}

				// assignments <assign> --> <listofvariables> "=" <exp>
				// 0 child stores variable name, 1 stores the value that will be assigned
				else if (nod.method == 90) {
					makeQuad(nod.method, nod.children.get(1).temp, null,
							nod.children.get(0).temp, nod);
					// System.err.println
					// ("assign quad: "+nod.children.get(1).temp);
				}

			}

			else { // >=3
				Object temp = genTemp();
				int iop = nod.children.get(1).method;

				Object lastTemp = nod.children.get(0).temp;

				// math and logic ops
				if (iop > 400 && iop < 500) {
					for (int i = 1; i < childnum - 1; i += 2) {
						temp = genTemp();

						iop = nod.children.get(i).method;
						if (iop > 400 && nod.method < 500) { // remove check?
							Quadruple q = new Quadruple(iop, lastTemp,
									nod.children.get(i + 1).temp, temp);

							nod.temp = temp;
							lastTemp = temp;

							quads.add(q);
						}
					}

				}

				// if stat -> exp ifspot statement [else statement endelse]
				// 100 -> 0)exp 1)900 2)statement [3)102 4)statement 5)901]
				else if (nod.method == 100) { // method of ifstat

					Node exp = nod.children.get(0);
					Node jumpFalse = nod.children.get(1);

					// System.out.println (jumpFalse.method); // check

					Quadruple jf = jumpFalse.pointArray;
					jf.var2 = exp.temp;

					if (childnum <= 3) {
						jf.var1 = numInQuad + 1; // this should be last spot.
					} else { // there's an else
						Node jumpTrue = nod.children.get(3);
						Quadruple jTrue = jumpTrue.pointArray;

						jf.var1 = quads.indexOf(jTrue);
						// System.err.println ("index of "+jf.var1);

						jTrue.var1 = numInQuad + 1;
						jTrue.var2 = exp.temp;
					}
				}

				// <while> --> beforeexp <exp> jumpfalse <statement> jump
				else if (nod.method == 80) {
					Node whileSpot = nod.children.get(0);
					Node exp = nod.children.get(1);
					Node jumpFalse = nod.children.get(2);

					Quadruple jf = jumpFalse.pointArray;
					jf.var2 = exp.temp;
					jf.var1 = numInQuad + 1; // this should be after jump

					Node jump = nod.children.get(4);

					//System.err.println("the jump Node:" + jump.method); // test

					Quadruple jumpQ = jump.pointArray;
					Quadruple beforeWhile = whileSpot.pointArray;

					jumpQ.var1 = quads.indexOf(beforeWhile);

				}

			}
		}
	}

	// gen temp values for later lookup in hashmap - all as string..
	Integer tempor = 10;

	private Object genTemp() {
		tempor++;
		// return tempor;
		String t = "temp*" + tempor.toString();
		return t;
	}

	public void makeQuad(Integer op, Object v1, Object v2, Object result,
			Node nod) {
		Quadruple q = new Quadruple(op, v1, v2, result);

		quads.add(q);
		// check set
		nod.temp = result;

		numInQuad = quads.indexOf(q);

		// System.out.println ("NIQ:"+numInQuad);
		// nod.pointArray = numInQuad;
	}

	public ArrayList<Quadruple> getQuadArray() {
		return quads;
	}

}
