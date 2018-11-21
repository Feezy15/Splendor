import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	private ArrayList<Card> deck;
	public Deck(){
		deck = new ArrayList<Card>();
	}
	public void addCard(String line) {
		deck.add(new Card(line));	
	}
	public void shuffle(){
		Collections.shuffle(deck);
	}
	public Card draw(){
		if(deck.size() > 0)
			return deck.remove(deck.size()-1);
		return null;
	}
	public Card getCard(int id){
		for(Card x: deck)
			if(x.getID() == id)
				return x;
		return null;
	}
}
