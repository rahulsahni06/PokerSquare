import java.util.*;

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
    private Card[][] bestChild;
    private int bestScore = 0;
    private int bestGeneration = 0;

    private static boolean verboseLogging = false;

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
        bestChild = null;
        bestScore = 0;
        bestGeneration = 0;
    }

    @Override
    public int[] getPlay(Card card, long millisRemaining) {
        return new int[0];
    }

    /* (non-Javadoc)
     * @see PokerSquaresPlayer#getPlay(Card, long)
     */
    public int[] getPlay(Card card, long millisRemaining, Stack<Card> deck) {

        long startMillis = System.currentTimeMillis();

        if(isFirstTime) {
            isFirstTime = false;

            Stack<Card> copiedDeck = new Stack<Card>();
            copiedDeck.addAll(deck);

            Card[][] cards = new Card[SIZE][SIZE];
            cards[0][0] = card;
            for(int i = 0; i<SIZE; i++) {
                for(int j = 0; j<SIZE; j++) {
                   if(i == 0 && j == 0) {
                       continue;
                   }
                   cards[i][j] = copiedDeck.pop();
                }
            }


            Population population = new Population(500, 0.15f, cards);

            while(System.currentTimeMillis() - startMillis < 25 * 1000) {
                generations++;

                population.calculateFitness(pokerSquaresPointSystem);
                population.generate();

                int index = population.getBestFit(pokerSquaresPointSystem);
                int score = pokerSquaresPointSystem.getScore(population.getChild(index));

                if(verboseLogging) {
                    System.out.println("Generation: " + generations + ": " + score);
                }

                if(bestScore < score) {
                    bestScore = score;
                    bestChild = population.getChild(index);
                    bestGeneration = generations;
                    saveBestChild(bestChild);

                }
                if(score <= bestScore && generations % 500 == 0) {
                    float newMutation = population.getMutationRate() * 1.25f;
                    if(newMutation > 0.8) {
                        newMutation = 0.15f;
                    }
                    population.setMutationRate(newMutation);

                }
            }

            if(verboseLogging) {
                System.out.println("Final mutation: " + population.getMutationRate());
                System.out.println("Best generation: " + bestGeneration + ": " + bestScore);
            }
        }

        int rowMajorPos = bestChildMap.get(card);
        return new int[]{rowMajorPos / 5, rowMajorPos % 5};

    }


    private void saveBestChild(Card[][] child) {
        bestChildMap.clear();
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

        verboseLogging = true;

        int gameCount = 1;
        int[] scores = new int[gameCount];
        int total = 0;
        for(int i = 0; i<gameCount; i++) {
            scores[i] = new PokerSquares(gp, system).play();
            total += scores[i];
        }
        System.out.println("Average: "+total/scores.length);
    }

}
