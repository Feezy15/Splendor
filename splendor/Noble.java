import java.io.IOException;
import java.util.Scanner;

public class Noble {
	private static int points = 3;
	//white blue green red black
	private int[] cost;
	private int id;
	private String flavor;
	public Noble(String line){//IOException
		Scanner sc = new Scanner(line);
		id = sc.nextInt();
		cost = new int[5];
		String costs = sc.next();
		for(int x = 0; x < costs.length(); x++)
			cost[x] = Integer.parseInt(costs.substring(x, x+1));
		flavor = sc.nextLine().trim();
	}
	public int getPoints(){	
		return points;
	}
	public int getID(){
		return id;
	}
	public int[] getCost(){
		return cost;
	}
	public String getFlavor(){
		return flavor;
	}
	public String toString(){
		String output = flavor + " ID: " + id + " Cost:";
		if(cost[0] > 0)
			output += " Diamond-" + cost[0];
		if(cost[1] > 0)
			output += " Sapphire-" + cost[1];
		if(cost[2] > 0)
			output += " Emerald-" + cost[2];
		if(cost[3] > 0)
			output += " Ruby-" + cost[3];
		if(cost[4] > 0)
			output += " Onyx-" + cost[4];
		return output;
	}
}
