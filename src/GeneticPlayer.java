import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * FlushPlayer - a simple example implementation of the player interface for PokerSquares that
 * attempts to get flushes in the first four columns.
 * Author: ________, based on code provided by Todd W. Neller and Michael Fleming
 */
public class GeneticPlayer implements PokerSquaresPlayer {

    private final int SIZE = 5; // number of rows/columns in square grid
    private Card[][] grid = new Card[SIZE][SIZE]; // grid with Card objects or null (for empty positions)
    private boolean isFirstTime = true;
    private int generations = 0;
    private PokerSquaresPointSystem pokerSquaresPointSystem;
    private final HashMap<Card, Integer> bestChildMap = new HashMap<>();

    /* (non-Javadoc)
     * @see PokerSquaresPlayer#setPointSystem(PokerSquaresPointSystem, long)
     */
    @Override
    public void setPointSystem(PokerSquaresPointSystem system, long millis) {
        this.pokerSquaresPointSystem = system;
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
            }
        }

        bestChildMap.clear();
        isFirstTime = true;
        generations = 0;
    }

    @Override
    public int[] getPlay(Card card, long millisRemaining) {
        return new int[0];
    }

    /* (non-Javadoc)
     * @see PokerSquaresPlayer#getPlay(Card, long)
     */
    public int[] getPlay(Card card, long millisRemaining, Stack<Card> deck) {

        if(isFirstTime) {
            isFirstTime = false;

            Stack<Card> copiedDeck = new Stack<Card>();
            copiedDeck.addAll(deck);
//            Iterator<Card> deckIterator = deck.iterator();


            Card[][] cards = new Card[SIZE][SIZE];
            cards[0][0] = card;
            for(int i = 0; i<SIZE; i++) {
                for(int j = 0; j<SIZE; j++) {
                   if(i == 0 && j == 0) {
                       continue;
                   }
//                   cards[i][j] = deckIterator.next();
                   cards[i][j] = copiedDeck.pop();
                }
            }

//            pokerSquaresPointSystem.printGrid(cards);
//
//            int score = pokerSquaresPointSystem.getScore(cards);
//            System.out.println("initial"+": "+score);



            Population population = new Population(500, 0.1f, cards);
//            pokerSquaresPointSystem.printGrid(population.getPopulation());

            while(generations <= 500) {
                generations++;

                population.calculateFitness(pokerSquaresPointSystem);
                population.generate();

//                System.out.println("Generation: "+generations);
//                int i = 1;
//                for(Dna newChild : population.getPopulation()) {
//                    int score = pokerSquaresPointSystem.getScore(newChild.getCards());
//                    System.out.println(""+i+": "+score);
//                    i++;
//                }
//                System.out.println("\n");
//
//                if(generations == 1) {
//                    for(i = 0; i<10; i++) {
//                        pokerSquaresPointSystem.printGrid(population.getChild(i));
//                    }
//                }
            }

            int index = population.getBestFit(pokerSquaresPointSystem);
            saveBestChild(population.getChild(index));
//            pokerSquaresPointSystem.printGrid(population.getChild(index));
//            for(int i = 0; i<10; i++) {
//                pokerSquaresPointSystem.printGrid(population.getChild(i));
//            }
        }

//        int play = plays.pop(); // get the next random position for play
        int rowMajorPos = bestChildMap.get(card);
        return new int[]{rowMajorPos / 5, rowMajorPos % 5};

    }


    private void saveBestChild(Card[][] child) {
        for(int i = 0; i < PokerSquares.SIZE; i++) {
            for(int j = 0; j < PokerSquares.SIZE; j++) {
                bestChildMap.put(child[i][j], i * SIZE + j);
            }
        }
    }

    /* (non-Javadoc)
     * @see PokerSquaresPlayer#getName()
     */
    @Override
    public String getName() {
        return "GeneticPlayer";
    }

    /**
     * Demonstrate FlushPlayer play with British point system.
     * @param args (not used)
     */
    public static void main(String[] args) {
        PokerSquaresPointSystem system = PokerSquaresPointSystem.getBritishPointSystem();
        System.out.println(system);
        GeneticPlayer gp = new GeneticPlayer();
//        new PokerSquares(gp, system).play(); // play a single game
        // new PokerSquares(new GeneticPlayer(), system).play(); // play a single game
        // new PokerSquares(new GeneticPlayer(), system).play(); // play a single game
        // new PokerSquares(new GeneticPlayer(), system).play(); // play a single game
        // new PokerSquares(new GeneticPlayer(), system).play(); // play a single game
        // new PokerSquares(new GeneticPlayer(), system).play(); // play a single game
        // new PokerSquares(new GeneticPlayer(), system).play(); // play a single game
        // new PokerSquares(new GeneticPlayer(), system).play(); // play a single game
       new PokerSquares(gp, system).play(); // play a single game
       new PokerSquares(gp, system).play(); // play a single game
       new PokerSquares(gp, system).play(); // play a single game
       new PokerSquares(gp, system).play(); // play a single game
       new PokerSquares(gp, system).play(); // play a single game
       new PokerSquares(gp, system).play(); // play a single game
       new PokerSquares(gp, system).play(); // play a single game
    }

}
