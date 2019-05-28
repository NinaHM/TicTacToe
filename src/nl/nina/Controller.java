package nl.nina;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class Controller {

	private static final String PLAYER_1 = "X";
	private static final String PLAYER_2 = "O";
	private Button[] buttons;
	private Model model;
	
	private static boolean pvp;
	private boolean isPlayer1;

	@FXML
	private Button b0, b1, b2, b3, b4, b5, b6, b7, b8;

	public Controller() {
		this.model = new Model();
		isPlayer1 = true;
	}

	@FXML
	public void initialize() {
		buttons = new Button[] { b0, b1, b2, b3, b4, b5, b6, b7, b8 };
	}

	/**
	 * Takes action when one of the nine buttons is pressed.
	 */
	public void buttonClickHandler(ActionEvent e) {
		Button button = (Button) e.getSource();
		int cell = findButton(button);
		boolean gameEnd = false;
		
		if(button.getText().equals("") && isPlayer1) {
			button.getStyleClass().add("player1-button");
			button.setText(PLAYER_1);
			model.registerTurn(cell, PLAYER_1);
			isPlayer1 = false;
			gameEnd = hasGameEnded(PLAYER_1, cell);
			if (!gameEnd && !pvp) {
				performComputerMove();
				isPlayer1 = true;
			}
		} else if(button.getText().equals("") && !isPlayer1 && pvp) {
			button.getStyleClass().add("player2-button");
			button.setText(PLAYER_2);
			model.registerTurn(cell, PLAYER_2);
			isPlayer1 = true;
			hasGameEnded(PLAYER_2, cell);
		} 		
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
	 * @param player	player who's turn it is
	 * @param cell		move that was made
	 * @return 			true if the game has ended
	 */
	private boolean hasGameEnded(String player, int cell) {
		if (model.hasPlayerWon(player, cell)) {
			if (player == PLAYER_1) {
				endMessage("Player 1 has won the game!");
				return true;
			} else if (player == PLAYER_2) {
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
	}

	/**
	 * Performs the necessary actions for the Computer opponent to make a move.
	 */
	private void performComputerMove() {
		int move = model.computerTurn();
		buttons[move].getStyleClass().add("player2-button");
		buttons[move].setText(PLAYER_2);
		model.registerTurn(move, PLAYER_2);
		hasGameEnded(PLAYER_2, move);
	}
	
	/*
	 * Handles the click events from the top menu.
	 */
	public void menuClickHandler(ActionEvent e) {
		MenuItem clickedMenu = (MenuItem) e.getTarget();
		if (clickedMenu.getText().equals("New game")) {
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
