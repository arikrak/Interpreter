import java.util.ArrayList;
import java.util.HashMap;


public class Interpret {

	ArrayList<Quadruple> quads;
	HashMap<Object, Object> map;

	public Interpret(ArrayList<Quadruple> quds){
		quads=quds;
		map = new HashMap<Object, Object>();

		int i = 0;
		while(i<quads.size())
		{
			//System.out.println ("interpreting*");
			Quadruple q = quads.get(i);

			Object ans = null;

			int a=-1;
			int b=-1;

			boolean ay = false;
			boolean be = false;

			int op = q.oper;

			Object obj1 = map.get(q.var1);
			Object obj2 = map.get(q.var2);



			if(q.var1 instanceof Boolean){
				ay = (Boolean) q.var1;
			}
			else if(obj1 instanceof Boolean){
				ay = (Boolean) obj1;
			}

			else if (q.var1 instanceof Integer){
				a = (Integer) q.var1;
			}
			else if(q.var1 instanceof String){
				String str1=(String) q.var1;

					a=getInt(q.var1, str1);
			}

			//next value
			if(q.var2 instanceof Boolean){
				be = (Boolean) q.var2;
			}
			else if(obj2 instanceof Boolean){
				be = (Boolean) obj2;
			}
			else if (q.var2 instanceof Integer){
				b = (Integer) q.var2;
			}
			else if(q.var2 instanceof String){
				String str2= (String) q.var2;
					b=getInt(q.var2, str2);
			}

				switch(op){

				//assign
				case 90:
					ans = a;
					break;
					
				case 110:
					System.out.println ("Print: " + (String)q.var1);
					break;

				//unary
				case 270:
					ans = -a;
					break;

				case 271:
					ans = +a; //0
					break;

					//jump false
				case 2000:
					be = (Boolean) map.get(q.var2);
					//if true do nothing, else jump
					i=(be)? i:a-1; //-1 since will be incrememnted
						break;

				case 430:
					//System.out.println ("adding" +a);
					ans = a+b;
					break;
				case 431:
					ans = a-b;
					break;

				case 450:
					ans = a*b;
					break;
				case 451:
					ans = a/b;
					break;
				case 452:
					ans = a%b;
					break;

				//booleans **
				//“<=”|”<”|”>”|”>=”
				case 410:
					ans = a<=b;
					break;
				case 411:
					ans = a<b;
					break;
				case 412:
					ans = a>b;
					break;
				case 413:
					ans = a>=b;
					break;

					//compare booleans
				case 490:
					ans = ay || be;
					break;
				case 491:
					ans = ay && be;
					break;
				case 492:
					ans = !ay;
					break;


				//| ^ & ~ ?
				case 493:
					ans = ay|be;
					break;
				case 494:
					ans = ay^be;
					break;
				case 495:
					ans = ay&be;
					break;
				case 496: //~ ?
					//ans = ~ay;
					break;

				//jumpfalse
				case 900:
					if(!be){i=a-1;}
					break;
					//jump true
				case 901:
					if(be){i=a-1;}
					break;
				case 902:
					break;
				case 905:
					i=a-1;
					break;
				default:
					System.out.println ("op for quad not found");
				}


				if(ans!=null){
				map.put(q.result, ans);
				System.out.println ("result:"+ans + " (var1:"+q.var1+")");
				}

				i++;
		}
	}



	private int getInt(Object var, String str1)
	{
		int num=-1;
		Character ch = str1.charAt(0);
		if(map.containsKey(var)){
			Object obj = (Integer) map.get(var);
			//can't be variable name*
			num = (Integer) obj;
			//System.out.println ("map lookup:"+str1+" "+num );
		}
		else if (Character.isDigit(ch)){
			num = Integer.parseInt( str1 );
		}
		else{
			System.out.println ("not a number");
		}
		return num;
	}



}