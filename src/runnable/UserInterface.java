package runnable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class UserInterface extends Frame implements ActionListener{

	private Label labelCount;    // Declare a Label component 
	private TextField textFieldCount; // Declare a TextField component 
	private Button buttonCount;   // Declare a Button component
	private int count = 0;     // Counter's value
	private TextField textFieldResult; // display user textField
	
	public UserInterface(){
		// "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
		// the components from left-to-right, and flow to next row from top-to-bottom.
		setLayout(new FlowLayout());

		labelCount = new Label("Counter"); // create a new label
		add(labelCount); // add label to container

		textFieldCount = new TextField("enter", 10); // crate a new text field to input text
		textFieldCount.setEditable(true); // set the text field to able to edit
		add(textFieldCount); // add text field to the container

		buttonCount = new Button("Count"); // create a new button
		add(buttonCount); // add button to the container

		textFieldResult = new TextField("display", 10);
		textFieldResult.setEditable(false);
		add(textFieldResult);
		
		
		// "btnCount" is the source object that fires an ActionEvent when clicked.
		// The source add "this" instance as an ActionEvent listener, which provides
		// an ActionEvent handler called actionPerformed().
		// Clicking "btnCount" invokes actionPerformed().
		buttonCount.addActionListener(this);

		setTitle("AWT Counter");  // "super" Frame sets its title
		setSize(250, 100);        // "super" Frame sets its initial window size

		setVisible(true);         // "super" Frame shows
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		++count; // Increase the counter value
		
		textFieldResult.setText(textFieldCount.getText());
		
		// Display the counter value on the TextField tfCount
		textFieldCount.setText(count + ""); // Convert int to String
		
		
	}

	public static void main(String args[]){

		UserInterface ui = new UserInterface();
	}
}
