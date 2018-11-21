import static java.lang.System.out;
public class PlayerTester {
	public static void main(String[] args){
		Player[] x = new Player[2];
		x[0] = new Player(0);
		x[0].addTokens("wug");
		out.println(x[0].checkTokens());
	}
	
}
