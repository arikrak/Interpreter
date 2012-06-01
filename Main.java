
import java.io.File;



public class Main {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		File f = new File("C:\\eclipse\\sample.txt");
		
		Splitter spl = new Splitter(f);
		Parse par = new Parse(spl);
		
//		System.out.println ("***************************\n" +
//				"Parsing is complete. Program will now go through the actual tree of Nodes and prinbt out their NodeCodes. \n" +
//				"Each Node has a node code that corresponds to a a specific variable. For example Declaration's code is 20 ");		

		//System.out.println ("******* the tree ******");
		//par.printTree();		
		
		System.out.println ("quads*******");
		Quadrupler quad = new Quadrupler(par.returnRoot());

		//Interpreter pc = new Interpreter(quad.getQuadArray());
		Interpret pc = new Interpret(quad.getQuadArray());
		
		System.out.println ("Hooray. Code has been compiled and interpreted! :)");
		

	}


}
