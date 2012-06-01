import java.util.ArrayList;


public class Node {


	ArrayList<Node> children = new ArrayList<Node>();
	int value;
	String data;
	int method;
	String strMethod;

	//for quads?
	Object temp; //String

	//store the quad or the int it points to 
	Quadruple pointArray;
	
	public Node(int val, String dat, int methd, String sMethod)
	{
		value=val;
		data=dat;
		method=methd; //Variable, operation, whatever
		strMethod = sMethod;

		temp=data; //try setting to it..
	}

	public void setNode(int i, Node n)
	{
		children.set(i, n);
	}

	public void addNode(Node n)
	{
		children.add(n);
		//checked Nodes always added in order
	}

	public void print()
	{
		System.out.print(" NodeCode: "+method +" "+ strMethod+". ");
		//+ " token:"+ value +" "+ data);
	}

	public ArrayList<Node> returnChildren()
	{
		return children;
	}





}
