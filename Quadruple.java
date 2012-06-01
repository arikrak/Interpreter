
public class Quadruple {


	Integer oper;
	Object result;
	Object var1; //so can hold int and strings* but may cause errors..**
	Object var2;
	//what values for quad
	public Quadruple(Integer op, Object v1, Object v2, Object rsult)
	{
		oper=op;
		result=rsult;
		var1=v1;
		var2=v2;
	}

	public void print()
	{
		System.out.println ("[op:"+oper+" vars:"+ var1 +" "+var2+ " result:"+ result +"]");
	}


}
