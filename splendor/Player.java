import java.awt.Color;
import java.util.ArrayList;

public class Player {
	private int id;
	private int[] tokens;
	private int[] bonuses;
	private ArrayList<Card> cards;
	private ArrayList<Noble> nobles;
	private ArrayList<Card> reserved;
	private int points;
	public Player(int i){
		tokens = new int[6];
		bonuses = new int[5];
		cards = new ArrayList<Card>();
		nobles = new ArrayList<Noble>();
		reserved = new ArrayList<Card>();
		points = 0;
		id = i;
	}
	public int getID(){
		return id+1;
	}
	public void buyReserved(Card a){
		cards.add(a);
		reserved.remove(a);		
		subtractTokens(tokenCost(a), difference(a));
		addResources(a.getColor());
		addPoints(a.getPointValue());
	}
	public void buy(Card a){
		cards.add(a);	
		subtractTokens(tokenCost(a), difference(a));
		addResources(a.getColor());
		addPoints(a.getPointValue());
	}
	public void addResources(int a){
		bonuses[a] += 1;
	}
	public void addSameTokens(String color){
		switch(color){
		case "d": tokens[0] += 2;
			break;
		case "s": tokens[1] += 2;
		break;
		case "e": tokens[2] += 2;
		break;
		case "r": tokens[3] += 2;
		break;
		case "o": tokens[4] += 2;
		break;
		}
	}
	public int[] getTokens(){
		return tokens;
	}
	public ArrayList<Noble> getNobles(){
		return nobles;
	}
	public ArrayList<Card> getCards(){
		return cards;
	}
	public int getCardAmount(){
		return cards.size();
	}
	public ArrayList<Card> getReserved(){
		return reserved;
	}
	public void addTokens(String colors){
		String[] temp = colors.split("");
		for(String x: temp){
			switch(x){
			case "d": tokens[0] += 1;
				break;
			case "s": tokens[1] += 1;
				break;
			case "e": tokens[2] += 1;
				break;
			case "r": tokens[3] += 1;
				break;
			case "o": tokens[4] += 1;
				break;
			}
		}
	}
	public int checkTokens(){
		int total = 0;
		for(int x: tokens)
			total += x;
		return total - 10;
	}
	public int[] tokenCost(Card a){
		int[] temp = new int[5];
		int[] cardcost = a.getCost();
		for(int x = 0; x < temp.length; x++){
			temp[x] = cardcost[x] - bonuses[x];
			while(temp[x] < 0)
				temp[x]++;
		}
		return temp;
	}
	public void addPoints(int a){
		points += a;
	}
	public boolean containsTokens(String colors){
		int d = 0, s = 0, e = 0, r = 0, o = 0;
		String[] temp = colors.split("");
		for(String x: temp){
			switch(x){
			case "d": d++;
				break;
			case "s": s++;
				break;
			case "e": e++;
				break;
			case "r": r++;
				break;
			case "o": o++;
				break;
			}
		}
		int[] returned = new int[]{d, s, e, r, o};
		for(int i=0; i<5; i++){
			if(tokens[i] < returned[i])
				return false;
		}
		return true;
	}
	public void subtractOverTokens(String colors){
		String[] temp = colors.split("");
		for(String x: temp){
			switch(x){
			case "d": tokens[0] -= 1;
				break;
			case "s": tokens[1] -= 1;
				break;
			case "e": tokens[2] -= 1;
				break;
			case "r": tokens[3] -= 1;
				break;
			case "o": tokens[4] -= 1;
				break;
			}
		}
	}
	public void subtractTokens(int[] cost, int difference){
		for(int i = 0; i < tokens.length-1; i++){
			tokens[i] -= cost[i];
			while(tokens[i] < 0)
				tokens[i]++;
		}
		tokens[5] -= difference;
	}
	public int[] returnTokens(Card a){
		int[] returns = new int[6];
		int[] costs = tokenCost(a);
		for(int i = 0; i < costs.length; i++)
			returns[i] = costs[i];
		returns[5] = difference(a);
		return returns;
	}
	public boolean canBuy(Card a){
		//checks if card is available to buy	
	    if (tokens[5] - difference(a) >= 0)
			return true;
		return false;	
	}
	public boolean canGetNoble(Noble n){
		int[] cost = n.getCost();
		for(int x = 0; x < cost.length; x++){
			if(bonuses[x] < cost[x])
				return false;
		}
		return true;
	}
	public int difference(Card a){
		int[] cost = tokenCost(a);
		int difference = 0;
		for(int x = 0; x < cost.length; x++){
			if(tokens[x] < cost[x])
				difference += cost[x] - tokens[x];
		}
		return difference;
	}
	public void reserve(Card a, boolean token){
		reserved.add(a);
		if(token)
			tokens[5] += 1;
	}
	public int getPoints(){
		return points;
	}
	public boolean canReserve(){
		return reserved.size() < 3;
	}
	public void addNoble(Noble n){
		nobles.add(n);
		points += 3;
	}
}
