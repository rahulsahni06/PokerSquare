/**
 * FlushPlayer - a simple example implementation of the player interface for PokerSquares that 
 * attempts to get flushes in the first four columns.
 * Author: ________, based on code provided by Todd W. Neller and Michael Fleming
 */
public class FlushPlayer implements PokerSquaresPlayer {

	private final int SIZE = 5; // number of rows/columns in square grid
	private final int NUM_POS = SIZE * SIZE; // number of positions in square grid
	private final int NUM_CARDS = Card.NUM_CARDS; // number of cards in deck
	private Card[][] grid = new Card[SIZE][SIZE]; // grid with Card objects or null (for empty positions)

	/**
	 * To keep track of empty spaces in grid
	 */
	private final int[][] emptyGrid = new int[SIZE][SIZE];

	/**
	 * Constant to indicate cell is empty
	 */
	private static final int CELL_EMPTY = 0;

	/**
	 * Constant to indicate cell is filled
	 */
	private static final int CELL_FILLED = 1;

	
	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#setPointSystem(PokerSquaresPointSystem, long)
	 */
	@Override
	public void setPointSystem(PokerSquaresPointSystem system, long millis) {
		// The FlushPlayer, like the RandomPlayer, does not worry about the scoring system.	
	}
	
	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#init()
	 */
	@Override
	public void init() {
		// clear grid
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				grid[row][col] = null;

				//marking all cells as empty
				emptyGrid[row][col] = CELL_EMPTY;
			}
		}
	}

	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#getPlay(Card, long)
	 */
	@Override
	public int[] getPlay(Card card, long millisRemaining) {
		int cardrow = 0;
		int cardcol = 0;

		boolean isPlacedInValidColumn = false;

		int cardrank = card.getRank();
		int cardsuit = card.getSuit();

		if (cardsuit == 0) { //Club
			for (int i = 0; i < SIZE; i++) {
				if (emptyGrid[i][0] == CELL_EMPTY) {
					grid[i][0] = card;
					cardrow = i;
					cardcol = 0;
					emptyGrid[i][0]  = CELL_FILLED;
					isPlacedInValidColumn = true;
					break;
				}
			}

		} else if (cardsuit == 1) { //Diamond
			for (int i = 0; i < SIZE; i++) {
				if (emptyGrid[i][1] == CELL_EMPTY) {
					grid[i][1] = card;
					cardrow = i;
					cardcol = 1;
					emptyGrid[i][1] = CELL_FILLED;
					isPlacedInValidColumn = true;
					break;
				}
			}

		} else if (cardsuit == 2) { //Heart
			for (int i = 0; i < SIZE; i++) {
				if (emptyGrid[i][2] == CELL_EMPTY) {
					grid[i][2] = card;
					cardrow = i;
					cardcol = 2;
					emptyGrid[i][2] = CELL_FILLED;
					isPlacedInValidColumn = true;
					break;
				}
			}

		} else { //Spade
			for (int i = 0; i < SIZE; i++) {
				if (emptyGrid[i][3] == CELL_EMPTY) {
					grid[i][3] = card;
					cardrow = i;
					cardcol = 3;
					emptyGrid[i][3] = CELL_FILLED;
					isPlacedInValidColumn = true;
					break;
				}
			}
		}

		if(!isPlacedInValidColumn) { // Check if valid column is filled, if it is then try filling 4th column otherwise try filling other columns

			if(emptyGrid[4][4] == CELL_FILLED) { // Check if column 4 is filled or not
				for (int i = 0; i < SIZE; i++) {
					 if (emptyGrid[i][0] == CELL_EMPTY) {
						grid[i][0] = card;
						cardrow = i;
						cardcol = 0;
						emptyGrid[i][0] = CELL_FILLED;
						break;
					} else if (emptyGrid[i][1] == CELL_EMPTY) {
						grid[i][1] = card;
						cardrow = i;
						cardcol = 1;
						emptyGrid[i][1] = CELL_FILLED;
						break;
					} else if (emptyGrid[i][2] == CELL_EMPTY) {
						grid[i][2] = card;
						cardrow = i;
						cardcol = 2;
						emptyGrid[i][2] = CELL_FILLED;
						break;
					} else if (emptyGrid[i][3] == CELL_EMPTY) {
						 grid[i][3] = card;
						 cardrow = i;
						 cardcol = 3;
						 emptyGrid[i][3] = CELL_FILLED;
						 break;
					 }
				}
			} else {
				for(int i = 0; i<SIZE; i++) {
					if (emptyGrid[i][4] == CELL_EMPTY) {
						grid[i][3] = card;
						cardrow = i;
						cardcol = 4;
						emptyGrid[i][4] = CELL_FILLED;
						break;
					}
				}
			}
		}

		int[] playPos = {cardrow, cardcol};
		return playPos;
	}

	/* (non-Javadoc)
	 * @see PokerSquaresPlayer#getName()
	 */
	@Override
	public String getName() {
		return "FlushPlayer";
	}

	/**
	 * Demonstrate FlushPlayer play with British point system.
	 * @param args (not used)
	 */
	public static void main(String[] args) {
		PokerSquaresPointSystem system = PokerSquaresPointSystem.getBritishPointSystem();
		System.out.println(system);
		new PokerSquares(new FlushPlayer(), system).play(); // play a single game
	}

}
