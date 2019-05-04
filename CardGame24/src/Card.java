import javafx.scene.image.Image;

public class Card {
	private String number, type;
	private Image image;
	private int value;
	
	public Card() {}
	
	public Card(String n, String t, int v) {
		this.number = n;
		this.type = t;
		this.value = v;
		image = new Image("/CardPictures/" + this.number + "_of_" + this.type + ".png");
	}
	
	//getters
	public String getNumber() {
		return this.number;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Image getImage() {
		return this.image;
	}
	
	public int getValue() {
		return this.value;
	}
	
	//setters
	public void setNumber(String n) {
		this.number = n;
	}
	
	public void setType(String t) {
		this.type = t;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public String toString(){
		return this.number + "_of_" + this.type;
	}
}