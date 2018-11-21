import java.awt.Color;
import java.io.File;
import java.util.Scanner;

public class Card{
	private int id;
	private int color;
	//white blue green red black
	private int[] cost;
	private int level;
	private int pointValue;
	private String flavor;
	
	public Card(String line){
		Scanner sc = new Scanner(line);
		id = sc.nextInt();
		switch(sc.next()){
		case "k": color = 4;
			break;
		case "u": color = 1;
			break;
		case "w": color = 0;
			break;
		case "g": color = 2;
			break;
		case "r": color = 3;
			break;
		}
		String costs = sc.next();
		cost = new int[5];
		for(int x=0; x<costs.length(); x++)
			cost[x] = Integer.parseInt(costs.substring(x, x+1));
		level = sc.nextInt();
		pointValue = sc.nextInt();
		flavor = sc.next();
	}
	
	public int getID(){
		return id;
	}
	public int getColor(){
		return color;
	}
	public int[] getCost(){
		return cost;
	}
	public int getLevel(){
		return level;
	}
	public int getPointValue(){
		return pointValue;
	}
	public String getFlavor(){
		return flavor;
	}
	public String toString(){
		String output = flavor + " ID: " + id + " " + "Resource:";
		switch(color){
		case 0: output += " Diamond";
			break;
		case 1: output += " Sapphire";
			break;
		case 2: output += " Emerald";
			break;
		case 3: output += " Ruby";
			break;
		case 4: output += " Onyx";
			break;
		}
		output += " Cost:";
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
		output += " Points: " + pointValue;
		return output;
	
	}
}
