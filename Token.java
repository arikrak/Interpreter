
public class Token {
	
	String str;
	int type;
	
	
	public Token(String s, int t){
		str = s;
		type = t;
	}
	
	public String getString(){
		return str;
	}
	
	public int getType(){
		return type;
	}

}
