package nl.nina;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Window {
	
	private JFrame frame;
	private JTextField textField;
	private JPanel buttonsPanel; 
	private JButton[] buttons;
	private ActionListener[] actionListenerList;
	private Controller controller;
	
	public Window() {
		controller = new Controller(this);
		initialize(); 
	}
	
	/** 
	 * Starts the methods to create the frame and all its components.
	 */
	private void initialize() {
		createFrame();
		addButtonsPanel();
		addTextField(); 
		addButtons();
		frame.setVisible(true);
	}
	
	/** 
	 * Creates JPanel on which the Tic Tac Toe board is displayed.
	 */
	private void createFrame() {
		frame = new JFrame("Boter Kaas en Eieren");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
	}
	
	/**
	 * Adds a JPanel (the buttonsPanel) to the frame
	 */
	private void addButtonsPanel() {
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(3, 3));
		buttonsPanel.setPreferredSize(new Dimension(400, 370));
		frame.add(buttonsPanel);
	}
	
	/**
	 * Adds 9 JButtons and their ActionListeners to the buttonsPanel.
	 * These are the cells on which the player can make his or her move.
	 */
	private void addButtons() {
		buttons = new JButton [9];
		actionListenerList = new ActionListener[9];
		for(int i=0; i<9; i++) {
			JButton button = new JButton(); 
			ActionListener actionListener = controller;
			actionListenerList[i] = actionListener;
			button.addActionListener(actionListener);
			button.setFont(new Font("Tahoma", Font.PLAIN, 50));
			button.setForeground(Color.RED);
			buttons[i] = button;
			buttonsPanel.add(button);
			}
		}
	
	/**
	 * Adds a text field to the bottom of the frame.
	 */
	private void addTextField() {
		textField = new JTextField(); 
		textField.setPreferredSize(new Dimension(400, 30));
		textField.setText("Playing game");
		textField.setEditable(false);
		frame.add(textField, BorderLayout.PAGE_END);
	}
	
	/**
	 * Updates the button with a text and removes
	 * the ActionListener, so that it cannot be used any more.
	 * @param index		the index of the button in the buttons array
	 * @param text		the text to be placed on the button
	 */
	public void updateButton(int index, String text) {
		JButton button = buttons[index];
		button.setText(text);
		button.removeActionListener(controller);
	}
	
	/**
	 * Searches the array of buttons to find the index of a button.
	 * @param button			the button to search for
	 * @return					index of the button
	 */
	public int findButton(JButton button) {
		for (int i=0; i<buttons.length; i++) {
			if (buttons[i] == button) { 
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Display a dialog box with the end message, when the game has ended.
	 * @param endState		the end state of the game, either the name of the winner or tie
	 */
	public void endMessage(String endState) {
		if(endState == "X") {
			JOptionPane.showMessageDialog(frame,  "Player 1 won the game!");
		} else if(endState == "O") {
			JOptionPane.showMessageDialog(frame,  "Computer won the game!");
		} else {
			JOptionPane.showMessageDialog(frame,  "Game was a tie!");
		}
		restartPanel();
	}
	
	/**
	 * Adds a new set of buttons to the frame, to start a new Tic Tac Toe game.
	 */
	private void restartPanel() {
		frame.remove(buttonsPanel);
		addButtonsPanel();
		addButtons();
        frame.validate();
	}
}
