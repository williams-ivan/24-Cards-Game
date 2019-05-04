import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.script.ScriptEngineManager;


import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class CardGameGUIController {

	ScriptEngineManager mgr = new ScriptEngineManager();
    ScriptEngine engine = mgr.getEngineByName("JavaScript");
    
    static private String[] cardNumber = {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"}, cardType = {"clubs", "diamonds", "hearts", "spades"}, operators = {"+", "-", "*", "/"};
    
    Card[] cards = new Card[] {new Card("3", "hearts", 3), new Card("king", "clubs", 13), new Card("4", "diamonds", 4), new Card("2", "clubs", 2)};

	@FXML
	private ImageView cardInWindow1;

	@FXML
	private ImageView cardInWindow2;

	@FXML
	private ImageView cardInWindow3;

	@FXML
	private ImageView cardInWindow4;

	@FXML
	private TextField solutionTextField;

	@FXML
	private TextField expressionTextField;

	@FXML
	private Button refreshButton;

	@FXML
	private Button verifyButton;
	
	@FXML
	void aboutTheApplication(ActionEvent event) {
		JOptionPane.showMessageDialog(null,
				"Card Game 24 \n\nVersion: 2019-4 (1.1.1)\nAuthor: Phillip Moreno\n\nDescription: This game randomly generates four "
						+ "\ndifferent cards that can be used in a math \nequation based on their value and total to 24.\n\n",
				"About The Application", 1);
	}

	@FXML
	void closeWindow(ActionEvent event) {
		System.exit(0);
	}

	@FXML
	void findSolution(ActionEvent event) throws ScriptException{
		int[] n = {cards[0].getValue(), cards[1].getValue(), cards[2].getValue(), cards[3].getValue()};
		boolean[] deleted = new boolean[n.length];
		ArrayList<String> list = getInstructions(-1, -1, -1, n, deleted);
		if(!list.isEmpty()) {
			String str = getEquation(n, list);
			if(engine.eval(str).equals(24)) {
				solutionTextField.setText(str);
			}
			else {
				solutionTextField.setText("No solution");
			}
		}
		else {
			solutionTextField.setText("No solution");
		}
	}
	
	public ArrayList<String> getInstructions(int i1, int i2, int i3, int[] n, boolean[] deleted) {
		int remain = 0, tmp = 0;
		for (int i = 0; i < deleted.length; i++) {
			if (!deleted[i]) {
				tmp = n[i];
				remain++;
			}
		}
		if (remain == 1) {
			if (Math.abs(tmp - 24) < 1E-10) {
				ArrayList<String> list = new ArrayList<>();
				list.add("parenthesis," + 1);
				list.add("number," + i1);
				list.add("operator," + i2);
				list.add("number," + i3);
				list.add("parenthesis," + 2);
				return list;
			}
			else {
				return new ArrayList<>();
			}
		}
		for (int f = 0; f < 4; f++) {
			for (int i = 0; i < 4; i++) {
				if (!deleted[i]) {
					for (int j = 0; j < 4; j++) {
						if (!deleted[j]) {
							if (i != j) {
								int[] tmpn = n.clone();
								boolean[] tmpDeleted = deleted.clone();
								tmpDeleted[j] = true;
								try {
									tmpn[i] = evaluate(n[i], n[j], operators[f]);
								} catch (Exception e) {
									break;
								}
								ArrayList<String> list = getInstructions(i, f, j, tmpn, tmpDeleted), tmpList = new ArrayList<>();
								if(list.size() > 0) {
									for(int o = 0; o < list.size(); o++) {
										if(list.get(o).substring(0, list.get(o).indexOf(",")).equals("number") && list.get(o).substring(list.get(o).indexOf(",") + 1, list.get(o).length()).equals(Integer.toString(i1))) {
											tmpList.add("parenthesis," + 1);
											tmpList.add("number," + i1);
											tmpList.add("operator," + i2);
											tmpList.add("number," + i3);
											tmpList.add("parenthesis," + 2);
										}
										else {
											tmpList.add(list.get(o));
										}
									}
									return tmpList;
								}
							}
						}
					}
				}
			}
		}
		return new ArrayList<>();
	}
	
	public String getEquation(int[] n, ArrayList<String> list) {
		String str = "";
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).substring(0, list.get(i).indexOf(",")).equals("number")) {
				str += n[Integer.parseInt(list.get(i).substring(list.get(i).indexOf(",") + 1, list.get(i).length()))];
			}
			else if(list.get(i).substring(0, list.get(i).indexOf(",")).equals("operator")) {
				str += operators[Integer.parseInt(list.get(i).substring(list.get(i).indexOf(",") + 1, list.get(i).length()))];
			}
			else if(list.get(i).substring(0, list.get(i).indexOf(",")).equals("parenthesis")) {
				if(list.get(i).substring(list.get(i).indexOf(",") + 1, list.get(i).length()).equals("1")) {
					str += "(";
				}
				else {
					str += ")";
				}
			}
		}
		return str;
	}
	
	public int evaluate(int x, int y, String s) {
		switch(s) {
		case "+":
			return x + y;
		case "-":
			return x - y;
		case "*":
			return x * y;
		case "/":
			return x/y;
		}
		return 0;
	}

	@FXML
	void refresh(ActionEvent event) {
		solutionTextField.setText("");
		expressionTextField.setText("");
		
		Random r = new Random();
		
		for(int i = 0; i < 4; i++) {
			int n1 = r.nextInt(13), n2 = r.nextInt(4);
			String number = cardNumber[n1], type = cardType[n2];
			boolean abundant = false;
			do {
				abundant = false;
				for(Card c: cards) {
					if(number.equals(c.getNumber()) && type.equals(c.getType())){
						abundant = true;
						n1 = r.nextInt(13);
						n2 = r.nextInt(4);
						number = cardNumber[n1];
						type = cardType[n2];
					}
				}
			}while(abundant == true);
			cards[i] = new Card(number, type, n1 + 1);
		}
		
		cardInWindow1.setImage(cards[0].getImage());
		cardInWindow2.setImage(cards[1].getImage());
		cardInWindow3.setImage(cards[2].getImage());
		cardInWindow4.setImage(cards[3].getImage());
	}

	@FXML
	void verify(ActionEvent event) throws ScriptException {
		int[] n = new int[13];
		for(int i = 0; i < 4; i++) {
			n[cards[i].getValue() - 1]++;
		}
		String expressionInput = expressionTextField.getText();
		if(expressionInput.contains(Integer.toString(cards[0].getValue())) && expressionInput.contains(Integer.toString(cards[1].getValue())) && expressionInput.contains(Integer.toString(cards[2].getValue())) && expressionInput.contains(Integer.toString(cards[3].getValue()))) {
			//for(int i = 0; i < 4; i++) {System.out.println("" + count(expressionInput, Integer.toString(cards[i].getValue())) + " ... " + n[cards[i].getValue() - 1]);}
			if(count(expressionInput, Integer.toString(cards[0].getValue())) == n[cards[0].getValue() - 1] && count(expressionInput, Integer.toString(cards[1].getValue())) == n[cards[1].getValue() - 1] && count(expressionInput, Integer.toString(cards[2].getValue())) == n[cards[2].getValue() - 1] && count(expressionInput, Integer.toString(cards[3].getValue())) == n[cards[3].getValue() - 1] && count(expressionInput, "") == 4) {
				if (engine.eval(expressionInput).equals(24)) {
					System.out.println(engine.eval(expressionInput));
					JOptionPane.showMessageDialog(null, "Success! The total is 24.", "Verify Math Equation", 1);
				} else {
					System.out.println(engine.eval(expressionInput));
					JOptionPane.showMessageDialog(null, "Oops! The total is not 24, Please try again.", "Verify Math Equation", 1);
				}
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Incorrect input. Please try again.", "Verify Math Equation", 1);
		}
	}
	
	public int count(String str, String n) {
		int count = 0;
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(str);
		while (m.find()) {
			if(n.equals("")) {
				count++;
			}
			else {
				if (m.group().equals(n)) {
					count++;
				}
			}
		}
		return count;
	}

	public void main(String[] args) {
		//refreshButton.fire();
	}
}
