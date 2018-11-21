import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import static java.lang.System.out;

public class GameState {
	private Player[] players;
	private Deck level1, level2, level3;
	private int[] tokens;
	private Card[][] grid;
	private ArrayList<Integer> winnerIDs;
	private NobleDeck nobles;
	public GameState() throws FileNotFoundException{
		initializePlayers();
		initializeGrid();
		winnerIDs = new ArrayList<Integer>();
		gameStart(true);
	}
	private void initializeTokens(int tokenmax){
		Scanner sc = new Scanner(System.in);
		tokens = new int[6];
		for(int i=0; i<5; i++)
			tokens[i] = tokenmax;
		tokens[5] = 5;
		//ask for tokens
	}
	private void initializePlayers() throws FileNotFoundException{
		Scanner sc = new Scanner(System.in);
		out.print("How many players? ");
		try{
			switch(sc.nextInt()){
				case 2:{
					players = new Player[2];
					initializeTokens(4);
					initializeDeck(7);
					break;
				}
				case 3:{
					players = new Player[3];
					initializeTokens(5);
					initializeDeck(6);
					break;
				}
				case 4:{
					players = new Player[4];
					initializeTokens(7);
					initializeDeck(5);
					break;
				}
				default: {
					out.println("default 4");
					players = new Player[4];
					initializeTokens(7);
					initializeDeck(5);
				}
			}
		}
		catch(InputMismatchException e){
			out.println("default 4");
			players = new Player[4];
			initializeTokens(7);
			initializeDeck(5);
		}
	for(int x = 0; x < players.length; x++)
		players[x] = new Player(x);
	}
	private void initializeGrid() {
		grid = new Card[3][4];
		for(int x = 0; x < grid.length; x++){
			for(int i = 0; i < grid[x].length; i++){
				if(x == 0) grid[x][i] = level3.draw();
				else if(x == 1) grid[x][i] = level2.draw();
				else grid[x][i] = level1.draw();
			}
		}
	}
	private void initializeDeck(int noblesRemoved) throws FileNotFoundException{
		level1 = new Deck();
		level2 = new Deck();
		level3 = new Deck();
		nobles = new NobleDeck();
		Scanner sc = new Scanner(new File("cardlist.txt"));
		for(int i = 0; i < 40; i++)
			level1.addCard(sc.nextLine().trim());
		sc.nextLine();
		level1.shuffle();
		for(int i = 0; i < 30; i++)
			level2.addCard(sc.nextLine().trim());
		sc.nextLine();
		level2.shuffle();
		for(int i = 0; i < 20; i++)
			level3.addCard(sc.nextLine().trim());
		sc.nextLine();
		level3.shuffle();
		for(int i = 0; i < 10; i++)
			nobles.addCard(sc.nextLine());
		nobles.shuffle();
		for(int i = 0; i < noblesRemoved; i++)
			nobles.removeCard();
	}
	public void printBoard(){
		out.println("Gems: 0-Diamond 1-Sapphire 2-Emerald 3-Ruby 4-Onyx\nNobles");
		for(int i=0; i<nobles.length(); i++)
			out.print(nobles.get(i) + "\n");
		out.println();
		out.println("Tokens ");
		for(int x = 0; x < tokens.length; x++){
			if(x == 0) out.print("Diamond: " + tokens[0]);
			else if(x == 1) out.print(" Sapphire: " + tokens[1]);
			else if(x == 2) out.print(" Emerald: " + tokens[2]);
			else if(x == 3) out.print(" Ruby: " + tokens[3]);
			else if(x == 4) out.print(" Onyx: " + tokens[4]);
			else out.println(" Wild: " + tokens[5]+ "\n");
		}
		for(int i=0; i<3; i++){
			if(i == 0) out.println("Level 3 Development Cards");
			else if(i == 1) out.println("Level 2 Development Cards");
			else out.println("Level 1 Development Cards");
			for(int x=0; x<4; x++){
				out.print(grid[i][x] + "\n");
			}
			out.println();
		}
	}
	private void printPlayerTokens(Player x) {
		int[] z = x.getTokens();
		out.println("\nPlayer " + x.getID() + "'s gems: Diamond-" + z[0] + " Sapphire-" + z[1] + " Emerald-" + z[2] + " Ruby-" + z[3] + " Onyx-" + z[4] + " Wild-" + z[5]);
	}
	private boolean checkUnique(String colors){
	    for(char c : colors.toCharArray()){
	        if(!(colors.indexOf(c) == colors.lastIndexOf(c)))
	            return false;
	    }
	    return true;
	}
	/*private boolean sameTokens(String colors){
		return colors.charAt(0) == colors.charAt(1);
	}*/
	public boolean gameEnd(){
		int highest = 0;
		int numCards = Integer.MAX_VALUE;
		ArrayList<Player> qualified = new ArrayList<Player>();
		ArrayList<Player> samePoints = new ArrayList<Player>();
		for(Player x: players){
			if(x.getPoints() >= 15)
				qualified.add(x);
		}
		if(qualified.isEmpty())
			return false;
		if(qualified.size() == 1){
			winnerIDs.add(qualified.get(0).getID());
			return true;
		}
		for(int i=0; i<qualified.size(); i++){
			if(qualified.get(i).getPoints() > highest)
				highest = qualified.get(i).getPoints();
		}
		for(Player x: qualified){
			if(x.getPoints() == highest)
				samePoints.add(x);
		}
		if(samePoints.size() == 1){
			winnerIDs.add(samePoints.get(0).getID());
			return true;
		}
		for(int i=0; i<samePoints.size(); i++){
			if(samePoints.get(i).getCardAmount() < numCards)
				numCards = samePoints.get(i).getCardAmount();
		}
		for(Player x: samePoints){
			if(x.getCardAmount() == numCards)
				winnerIDs.add(x.getID());
		}
		return true;
	}
	public void gameStart(boolean continues){
		if(continues){
			for(Player x: players){
				boolean success = false;
				while(!success)
					success = playerAction(x);
				checkNobles(x);
			}
			if(gameEnd())
				gameStart(false);
			else
				gameStart(true);
		}
		else
			results();
	}
	public boolean containsNobleID(ArrayList<Noble> nobles, int id){
		for(Noble n: nobles){
			if(n.getID() == id)
				return true;
		}
		return false;
	}
	public void checkNobles(Player x){
		Scanner sc = new Scanner(System.in);
		ArrayList<Noble> nobleList = nobles.getNobles();
		ArrayList<Noble> canObtain = new ArrayList<Noble>();
		for(Noble n: nobleList)
			if(x.canGetNoble(n))
				canObtain.add(n);
		if(!canObtain.isEmpty()){
			if(canObtain.size() == 1){
				out.println("You recruited " + canObtain.get(0).getFlavor() + "to your cause!\n");
				nobles.removeCard(canObtain.get(0).getID());
				x.addNoble(canObtain.get(0));
			}
			else{
				out.println("You attracted " + canObtain.size() + " nobles! Enter the ID of the noble you want:\n");
				for(Noble n: canObtain)
					out.print(n + "\n");
				int id = 0;
				while(true){
					try{ 
						id = sc.nextInt();
					}
					catch(InputMismatchException e){
						out.println("Error in input... Input again\n");
					}
					if(containsNobleID(canObtain, id))
						break;
					else
						out.println("Invalid ID... Input again\n");
				}
				for(Noble n: canObtain){
					if(n.getID() == id){
						out.println("You recruited " + n.getFlavor() + "to your cause!\n");
						nobles.removeCard(n.getID());
						x.addNoble(n);
						break;
					}	
				}
			}
		}
	}
	public void results() {
		// TODO Auto-generated method stub
		String output = "";
		if(winnerIDs.size() == 1)
			out.println("Player " + winnerIDs.get(0) + " has won!");
		else{
			output = "Players ";
			for(int x: winnerIDs)
				output += x + ", ";
			output = output.substring(0, output.length() - 2);
			output += " have won!";
			out.println(output);
		}
	}
	public void printActions(){
		out.println("1: Take 3 gems of different colors");
		out.println("2: Take 2 gems of the same color");
		out.println("3: Reserve a card from board");
		out.println("4: Reserve a card from deck");
		out.println("5: Purchase a card");
	}
	private void subtractSameTokens(String color){
		switch(color){
		case "d": tokens[0] -= 2;
			break;
		case "s": tokens[1] -= 2;
		break;
		case "e": tokens[2] -= 2;
		break;
		case "r": tokens[3] -= 2;
		break;
		case "o": tokens[4] -= 2;
		break;
		}
	}
	private void subtractTokens(String colors){
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
	public void printPlayerResources(Player b){
		for(Player x: players){
			printPlayerTokens(x);
			ArrayList<Card> cards = x.getCards();
			out.println("Player " + x.getID() + "'s cards: ");
			for(Card c: cards)
				out.println(c);
			if(x.equals(b)){
				ArrayList<Card> reserved = x.getReserved();
				out.println("Player " + x.getID() + "'s reserved cards: ");
				for(Card c: reserved)
					out.println(c);
			}
			else{
				out.println("Player " + x.getID() + " has " + x.getReserved().size() + " reserved card(s)");
			}
			ArrayList<Noble> nobles = x.getNobles();
			out.println("Player " + x.getID() + "'s nobles: ");
			for(Noble c: nobles)
				out.println(c);
		}
		out.println();
	}
	public boolean sameTokensAvailable(String y){
		switch(y){
		case "d": if(tokens[0] < 4) return false;
			break;
		case "s": if(tokens[1] < 4) return false;
			break;
		case "e": if(tokens[2] < 4) return false;
			break;
		case "r": if(tokens[3] < 4) return false;
			break;
		case "o": if(tokens[4] < 4) return false;
			break;
		}
		return true;
	}
	public boolean diffTokensAvailable(String x){
		String[] letters = x.split("");
		for(String s: letters)
			switch(s){
			case "d": if(tokens[0] == 0) return false;
				break;
			case "s": if(tokens[1] == 0) return false;
				break;
			case "e": if(tokens[2] == 0) return false;
				break;
			case "r": if(tokens[3] == 0) return false;
				break;
			case "o": if(tokens[4] == 0) return false;
				break;
			}
		return true;
	}
	public boolean checkDiffInput(String y){
		if(!y.matches("[dsero]+") || y.length() != 3){
			return false;
		}
		return true;
	}
	public boolean checkSameInput(String y){
		if(!y.matches("[dsero]+") || y.length() != 1){
			return false;
		}
		return true;
	}
	public boolean checkOverTokenInput(String y, int over){
		if(!y.matches("[dsero]+") || y.length() != over)
			return false;
		return true;
	}
	public boolean playerAction(Player x){
		Scanner sc = new Scanner(System.in);
		printBoard();
		printPlayerResources(x);
		out.println("Player " + x.getID() + ": Choose an action");
		printActions();
		int a = 0;
		try{ a = sc.nextInt(); }
		catch(InputMismatchException e){
			out.println("Error in input...\n");
			return false;
		}
		if( a < 1 || a > 5){
			out.println("Invalid action...\n");
			return false;
		}
		if (a == 1){
			out.println("Diamond(d) Sapphire(s) Emerald(e) Ruby(r) Onyx(o) Enter the corresponding letters (consecutively): ");
			sc.nextLine();
			String y = sc.nextLine().trim();
			y=y.toLowerCase();
			if(!checkDiffInput(y) || !checkUnique(y)){
				out.println("Error in input...\n");
				return false;
			}
			if(diffTokensAvailable(y)){
				x.addTokens(y);
				subtractTokens(y);
				if(x.checkTokens() > 0){
					out.println( x.checkTokens() + " gem(s) over: Enter which one(s) to remove, consecutively, until 10 remain");
					out.println("Diamond(d) Sapphire(s) Emerald(e) Ruby(r) Onyx(o)");
					printPlayerTokens(x);
					String colors = sc.next();
					colors = colors.toLowerCase();
					//check errors
					while(!checkOverTokenInput(colors, x.checkTokens()) || !x.containsTokens(colors)){
						out.println("Error in input... Input again\n");
						colors = sc.next();
						colors = colors.toLowerCase();
					}
					addTokens(colors);
					x.subtractOverTokens(colors);
				}
				return true;
			}
			out.println("One or more of the chosen gems unavailable...\n");
			return false;
		}
		else if(a == 2){
			out.println("Diamond(d) Sapphire(s) Emerald(e) Ruby(r) Onyx(o) Enter the corresponding letter: ");
			sc.nextLine();
			String y = sc.next().trim();
			y = y.toLowerCase();
			if(!checkSameInput(y)){
				out.println("Error in input...\n");
				return false;
			}
			if(sameTokensAvailable(y)){
				x.addSameTokens(y);
				subtractSameTokens(y);
				if(x.checkTokens() > 0){
					out.println( x.checkTokens() + " gem(s) over: Enter which one(s) to remove, consecutively, until 10 remain");
					out.println("Diamond(d) Sapphire(s) Emerald(e) Ruby(r) Onyx(o)");
					printPlayerTokens(x);
					String colors = sc.next();
					colors = colors.toLowerCase();
					//check errors
					while(!checkOverTokenInput(colors, x.checkTokens()) || !x.containsTokens(colors)){
						out.println("Error in input... Input again\n");
						colors = sc.next();
						colors = colors.toLowerCase();
					}
					addTokens(colors);
					x.subtractOverTokens(colors);
				}
				return true;
			}
			out.println("Chosen gems unavailable...\n");
			return false;
		}	
		else if(a == 3){
			out.println("Enter the ID of the desired card: ");
			int id;
			try{ id = sc.nextInt(); }
			catch(InputMismatchException e){
				out.println("Error in input...\n");
				return false;
			}
			if(x.canReserve()){
				for(int i=0; i<3; i++){
					for(int y=0; y<4; y++){
						if(grid[i][y].getID() == id){
								if(tokens[5] > 0){
									x.reserve(grid[i][y], true);							
									tokens[5]--;
								}
								else x.reserve(grid[i][y], false);
								if(grid[i][y].getLevel() == 1) grid[i][y] = level1.draw();
								else if(grid[i][y].getLevel() == 2) grid[i][y] = level2.draw();
								else grid[i][y] = level3.draw();
								return true;
						}
					}
				}
			}
			else{
				out.println("Failed - cannot have more than 3 cards reserved...\n");
				return false;
			}
			out.println("Failed - card not found...\n");
			return false;
		}
		else if(a == 4){
			out.println("Which level would you like to draw from (1 - 2 - 3)? ");
			int level;
			try{ level = sc.nextInt(); }
			catch(InputMismatchException e){
				out.println("Error in input...\n");
				return false;
			}
			if(level != 1 && level != 2 && level != 3){
				out.println("Invalid number\n");
				return false;
			}
			if(x.canReserve()){
				boolean token = tokens[5] > 0;
				if(level == 1)
					x.reserve(level1.draw(), token);
				else if(level == 2)
					x.reserve(level2.draw(), token);
				else
					x.reserve(level3.draw(), token);
				return true;
			}
		}	
		else{
			out.println("1: Purchasing from reserve \n2: Purchasing from board ");
			int choice = 0;
			try{ choice = sc.nextInt(); }
			catch(InputMismatchException e){
				out.println("Error in input...\n");
				return false;
			}
			if(choice != 1 && choice != 2){
				out.println("Error in input...\n");
				return false;
			}
			out.println("Enter the ID of the desired card: \n");
			int id = 0;
			
			try{ id = sc.nextInt(); }
			catch(InputMismatchException e){
				out.println("Error in input...\n");
				return false;
			}
			if(choice == 1){			
				for(Card c: x.getReserved())
					if(c.getID() == id){
						if(x.canBuy(c)){
							int[] returned = x.returnTokens(c);
							for(int i=0; i<tokens.length; i++)
								tokens[i] += returned[i];
							x.buyReserved(c);
							return true;
						}
						out.println("You lack the resources required for this card...\n");
						return false;
					}
				out.println("Failed - card not found...\n");
				return false;
			}
			else{
				for(int i=0; i<3; i++){  //fix buy case
					for(int y=0; y<4; y++){
						if(grid[i][y].getID() == id){
							if(x.canBuy(grid[i][y])){
								int[] returned = x.returnTokens(grid[i][y]);
								for(int b=0; b<tokens.length; b++)
									tokens[b] += returned[b];
								x.buy(grid[i][y]);
								if(grid[i][y].getLevel() == 1) grid[i][y] = level1.draw();
								else if(grid[i][y].getLevel() == 2) grid[i][y] = level2.draw();
								else grid[i][y] = level3.draw();
								return true;
							}
							out.println("You lack the resources required for this card...\n");
							return false;
						}
					}
				}
				out.println("Failed - card not found...\n");
				return false;
			}
		}
		return false;
	}
	private void addTokens(String colors) {
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
}
