package nl.nina;

public class Model {

	private static final int PLAYER_1 = 1;
	private static final int PLAYER_2 = 2;
	private static final int SIZE_BOARD = 3;
	private int[][] board;

	/**
	 * Create the array that keeps track of the board state.
	 */
	public Model() {
		board = new int[SIZE_BOARD][SIZE_BOARD];
	}
	
	/**
	 * Registers the move played on the board
	 * @param cell 	the cell which the player chose as his move
	 * @param isPlayer1 	the player who's turn it is
	 */
	public void registerTurn(int cell, boolean isPlayer1) {
		int value = determinePlayer(isPlayer1);
		board[cell % 3][cell / 3] = value;
	}
	
	/**
	 * Converts the boolean parameter to an integer, to determine the player
	 * @param isPlayer1		the player who's turn it is	
	 * @return 		an int that determines the player
	 */
	private int determinePlayer(boolean isPlayer1) {
		if(isPlayer1) {
			return PLAYER_1;
		} else {
			return PLAYER_2;
		}
	}
	
	/**
	 * @see Model#hasPlayerWon(int[][], int, int, int)
	 */
	public boolean hasPlayerWon(boolean isPlayer1, int cell) {	
		int player = determinePlayer(isPlayer1);
		int x = cell % 3;
		int y = cell / 3;
		
		return (hasPlayerWon(board, player, x, y));
	}
	
	/**
	 * Checks the board to see if there is a winner.
	 * @param board		the Tic Tac Toe board to play on
	 * @param player	the player for which to check the win
	 * @param x			the column of the most recently played move
	 * @param y			the row of the most recently played move
	 * @return 			true if the specified player has won
	 */
	private boolean hasPlayerWon(int[][] board, int player, int x, int y) {
		
		//vertical check
		for(int i=0 ; i<board.length; i++) {
	        if(board[x][i] != player) {
	        	 break;
	        }
            if(i == (board.length-1)){
                return true;
	            }
	        }
		
		//horizontal check
		for(int i=0 ; i<board.length; i++) {
	        if(board[i][y] != player){
	        	 break;
	        }
            if(i == (board.length-1)){
                return true;
	            }
	        }
		
        //diagonal check right
		if(x == y) {
			for(int i=0 ; i<board.length; i++) {
		        if(board[i][i] != player) {
		        	 break;
		        }
	            if(i == (board.length-1)){
	                return true;
	            }
		    }
		}
		
		//diagonal check left
		if((x + y) == (board.length - 1)) {
			for(int i=0 ; i<board.length; i++) {
		        if(board[i][(board.length-1)-i] != player){
		        	break;
		        }
	            if(i == (board.length-1)){
	                return true;
		        }
	        }
		}
		return false;
	}
	
	
	/**
	 * @see Model#isTie(int[][])
	 */
	public boolean isTie() {
		return isTie(board);
	}
	
	/**
	 * Checks the board to see if a tie is reached
	 * @param board		the Tic Tac Toe board to play on
	 * @return			true if there are no empty cells left
	 */
	private boolean isTie(int[][] board) {
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board.length; j++) {
				if(board[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks the best possible move for the computer
	 * @return 		the cell of the move the computer will make
	 */
	public int computerTurn() {
		int[][] newBoard = copyBoard();
		
		int bestScore = Integer.MIN_VALUE;
		int bestColumn = -1;
		int bestRow = -1;
		
		//checking all the cells, evaluating the empty ones 
		//and returning the cell with the highest score.
		for(int i = 0; i<newBoard.length; i++) {
			for(int j = 0; j<newBoard.length; j++) {
				if(newBoard[i][j] == 0) {
					newBoard[i][j] = PLAYER_2;
					int moveValue = bestMove(newBoard, PLAYER_2, i, j);
					newBoard[i][j] = 0;
					if(moveValue > bestScore) {
						bestScore = moveValue;
						bestColumn = i;
						bestRow = j;
					}
				}
			}
		}
		
		int bestMove = bestRow * 3 + bestColumn; 
		return bestMove;
	}
	
	/**
	 * Creates a copy of the current Tic Tac Toe board
	 * @return		a copy of the current Tic Tac Toe board
	 */
	private int[][] copyBoard() {
		int[][] newBoard = new int[board.length][board.length];
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board.length; j++) {
				newBoard[i][j] = board[i][j];
			}
		}
		return newBoard;
	}
	
	/**
	 * Considers all possible ways the game can go and returns 
	 * the best scores for each move.
	 * @param newBoard 		the Tic Tac Toe board to play on
	 * @param player		the player that is making the move
	 * @param column		the column of the cell that is being checked
	 * @param row			the row of the cell that is being checked
	 * @return				the score of a move
	 */
	private int bestMove(int[][] newBoard, int player, int column, int row) {
		
		//Returning a score when a win, loss or tie is reached
		if(hasPlayerWon(newBoard, player, column, row)) {
			if(player == PLAYER_2) {
				return 10;
			} else {
				return -10;
			}
		}
		
		if(isTie(newBoard)) {
			return 0;
		}
		
		//Making a new move for player X or player O and checking the score
		//by calling this method again.
		if(player == PLAYER_1) {
			int bestValue = Integer.MIN_VALUE;
			for(int i = 0; i<newBoard.length; i++) {
				for(int j = 0; j<newBoard.length; j++) {
					if(newBoard[i][j] == 0) {
						newBoard[i][j] = PLAYER_2;
						int moveValue = bestMove(newBoard, PLAYER_2, i, j);
						if(moveValue > bestValue) {
							bestValue = moveValue;
						}
						newBoard[i][j] = 0;
					}
				}
			}
			return bestValue;
		} else {
			int bestValue = Integer.MAX_VALUE;
			for(int i = 0; i<newBoard.length; i++) {
				for(int j = 0; j<newBoard.length; j++) {
					if(newBoard[i][j] == 0) {
						newBoard[i][j] = PLAYER_1;
						int moveValue = bestMove(newBoard, PLAYER_1, i, j);
						if(moveValue < bestValue) {
							bestValue = moveValue;
						}
						newBoard[i][j] = 0;
					}
				}
			}
			return bestValue;
		}
	}
}
