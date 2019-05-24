package nl.nina;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Controller implements ActionListener {
	
	private static final String PLAYER_1 = "X";
	private static final String PLAYER_2 = "O";
	private Window window;
	private Model model;
	
	public Controller (Window window) {
		this.window = window;
		this.model = new Model();
	}
	
	/**
	 * Invokes the Window and Model to take the appropriate action
	 * when a button is pressed.
	 */
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		int cell = window.findButton(button);
		window.updateButton(cell, PLAYER_1);
		model.registerTurn(cell, PLAYER_1);
		if(!hasGameEnded(PLAYER_1, cell)) {
			 performComputerMove(); 
		 }
	}
	
	/**
	 * Checks if the game has ended, either through a win or tie.
	 * @param player		player who's turn it is
	 * @param cell			move that was made
	 * @return				true if the game has ended
	 */
	private boolean hasGameEnded(String player, int cell) {
		if(model.hasPlayerWon(player, cell)) {
			endGame(player);
			return true;
		} 
		if(model.isTie()) {
			endGame("tie");
			return true;
		}
		return false;
	}
	
	/**
	 * Starts the end procedures for the other classes
	 * @param endState		the end state, either the player who won or tie
	 */
	private void endGame(String endState) {
		window.endMessage(endState);
		this.model = new Model();
	}
	
	/**
	 * Performs the necessary actions for the Computer opponent to make a move.
	 */
	private void performComputerMove() {
		int move = model.computerTurn();
		window.updateButton(move, PLAYER_2);
		model.registerTurn(move, PLAYER_2);
		hasGameEnded(PLAYER_2, move);
	}
}
