import java.util.ArrayList;
import java.util.Collections;

public class NobleDeck {
	private ArrayList<Noble> nobles;
	public NobleDeck(){
		nobles = new ArrayList<Noble>();
	}
	public void addCard(String line){
		nobles.add(new Noble(line));
	}
	public Noble removeCard(int id){
		Noble x = getNoble(id);
		nobles.remove(x);
		return x;
	}
	public void removeNoble(Noble x){
		nobles.remove(x);
	}
	public void shuffle(){
		Collections.shuffle(nobles);
	}
	public void removeCard(){
		nobles.remove(0);
	}
	public int length(){
		return nobles.size();
	}
	public Noble get(int index){
		return nobles.get(index);
	}
	public ArrayList<Noble> getNobles(){
		return nobles;
	}
	public Noble getNoble(int id){
		for(Noble x: nobles)
			if(x.getID() == id)
				return x;
		return null;
	}
}
