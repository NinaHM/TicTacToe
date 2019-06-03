package nl.nina;

import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Controller {

	private static final String PLAYER_1 = "X";
	private static final String PLAYER_2 = "O";
	private static boolean pvp;
	private boolean isPlayer1;
	private Button[] buttons;
	private Model model;

	@FXML
	private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;
	@FXML
	private Label turnLabel;

	public Controller() {
		this.model = new Model();
		isPlayer1 = true;
	}

	@FXML
	public void initialize() {
		buttons = new Button[] { b0, b1, b2, b3, b4, b5, b6, b7, b8 };
	}

	/**
	 * Handles the click event when one of the nine buttons is pressed.
	 */
	public void buttonClickHandler(ActionEvent e) {
		Button button = (Button) e.getSource();
		int cell = findButton(button);
		
		if(button.getText().equals("") && isPlayer1) {
			performMove(button, cell, PLAYER_1, "player1-button");
		} else if(button.getText().equals("") && !isPlayer1 && pvp) {
			performMove(button, cell, PLAYER_2, "player2-button");
		}
	}
	
	private void performMove(Button button, int cell, String player, String css) {
		button.getStyleClass().add(css);
		button.setText(player);
		model.registerTurn(cell, isPlayer1);
		turnLabel.setText((isPlayer1 ? "Player 2" : "Player 1") + "'s turn");
		
		if(!hasGameEnded(cell)) {
			if(isPlayer1 && !pvp) {
				isPlayer1 = !isPlayer1;
				performComputerMove();
			} else {
				isPlayer1 = !isPlayer1;
			}
		}
	}
	
	/**
	 * Performs the necessary actions for the Computer opponent to make a move.
	 */
	private void performComputerMove() {
		int move = model.computerTurn();
		performMove(buttons[move], move, PLAYER_2, "player2-button");
	}

	/**
	 * Searches the array of buttons.
	 * @param button	the button to search for
	 * @return			index of the button				
	 */
	private int findButton(Button button) {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == button) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks if the game has ended.
	 * @param cell 	move that was made
	 * @return 	true if the game has ended
	 */
	private boolean hasGameEnded(int cell) {
		if (model.hasPlayerWon(isPlayer1, cell)) {
			if (isPlayer1) {
				endMessage("Player 1 has won the game!");
				return true;
			} else {
				endMessage("Player 2 has won the game!");
				return true;
			}
		}
		if (model.isTie()) {
			endMessage("The game was a tie.");
			return true;
		}
		return false;
	}

	/**
	 * Displays the end of game message and choice options
	 * in a dialog box.
	 * @param message	message to be displayed	
	 */
	private void endMessage(String message) {
		turnLabel.setText(message);
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Game has ended");
		alert.setHeaderText(message);
		alert.setContentText("Would you like to play again?");
		ButtonType yes = new ButtonType("Yes");
		ButtonType no = new ButtonType("No");
		alert.getButtonTypes().setAll(yes, no);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == yes) {
			newGame();
		} else {
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].setDisable(true);
			}
			alert.close();
		}
	}
	
	/*
	 * Restarts the game.
	 */
	private void newGame() {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setText("");
			buttons[i].setDisable(false);
			buttons[i].getStyleClass().remove("player2-button");
		}
		this.model = new Model();
		isPlayer1 = true;
		turnLabel.setText("Player 1's turn");
	}
	
	/*
	 * Handles the click events from the top menu.
	 */
	public void menuClickHandler(ActionEvent e) {
		MenuItem clickedMenu = (MenuItem) e.getTarget();
		if (clickedMenu.getText().equals("Singleplayer")) {
			pvp = false;
			newGame();
		} else if (clickedMenu.getText().equals("Multiplayer")) {
			pvp = true;
			newGame();
		} else if (clickedMenu.getText().equals("Quit")) {
			Platform.exit();
		}
	}
	

	public void startClickHandler(ActionEvent e) throws IOException {
		Button button = (Button) e.getSource(); 
		
		if(button.getText().equals("Singleplayer")) {
			pvp = false;
		} else if (button.getText().equals("Multiplayer")){
			pvp = true;
		}
		
		Stage stage = (Stage) button.getScene().getWindow();
		Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
		Scene scene = stage.getScene();
		scene.getStylesheets().remove("Start.css");
		scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
		stage.getScene().setRoot(root);	
	}
}
