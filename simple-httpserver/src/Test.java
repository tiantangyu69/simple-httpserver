import java.util.regex.Pattern;


public class Test {
public static void main(String args[]){
	
	Pattern.compile("\\A([A-Z]+) +([^]+) +HTTP/([0-9\\.]+)$.*^Host:([^]+)$.*\r\n\r\n\\z");
	/*
	 * Pattern requestPattern = Pattern.compile(
			"\\A([A-Z]+) +([^]+) +HTTP/([0-9]\\.]+)$.*^Host:([^]+)$.*\r\n\r\n\\z", 
			Pattern.MULTILINE | Pattern.DOTALL);
	*/
}
}
