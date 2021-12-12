import java.util.*;

/**
 * RahulAmanGeneticPlayer - a Genetic algo implementation of the player interface for PokerSquares that
 * attempts to get flushes in the first four columns.
 * Author: Rahul, Aman based on code provided by Todd W. Neller and Michael Fleming
 */
public class RahulAmanGeneticPlayer implements PokerSquaresPlayer {

    private final int SIZE = 5; // number of rows/columns in square grid
    private Card[][] grid = new Card[SIZE][SIZE]; // grid with Card objects or null (for empty positions)

    private PokerSquaresPointSystem pokerSquaresPointSystem;

    //Flag to perform genetic algorithm only once
    private boolean isFirstTime = true;

    //Keeps track of how many generations we have created
    private int generations = 0;

    //Data structure to save best-fit child from each generation
    private final HashMap<Card, Integer> bestChildMap = new HashMap<>();

    //Keeps track of best child over all the generations
    private Card[][] bestChild;

    //Keeps track of score of best child over all the generations
    private int bestScore = 0;

    //Keeps track of generation which has the best child
    private int bestGeneration = 0;

    private HashMap<Integer, Boolean> unfilledGridPositions = new HashMap<>();

    private static boolean verboseLogging = false;

    private int unfilledPos;

    private static final int POPULATION_COUNT = 500;
    private static final int GENERATION_STUCK_LIMIT = 500;
    private static final float INITIAL_MUTATION_RATE = 0.15f;
    private static final float INCREASE_MUTATION_RATE = 1.25f;
    private static final float MAX_MUTATION_RATE = 0.8f;
    private static final float TIMEOUT = 5 * 1000;

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
                unfilledGridPositions.put(row*SIZE + col, false);
                System.out.println(""+(row*SIZE + col)+":"+unfilledGridPositions.get(row*SIZE + col));
            }
        }

        bestChildMap.clear();
//        unfilledGridPositions.clear();
        isFirstTime = true;
        generations = 0;
        bestChild = null;
        bestScore = 0;
        bestGeneration = 0;
    }

    @Override
    public int[] getPlay(Card card, long millisRemaining) {
        long startMillis = System.currentTimeMillis();

        if(isFirstTime) {
            isFirstTime = false;

            Stack<Card> deck = new Stack<Card>();
            for (Card c : Card.getAllCards())
                deck.push(c);
            Random random = new Random();
            Collections.shuffle(deck, random);

            Card[][] cards = new Card[SIZE][SIZE];
            cards[0][0] = card;
            for(int i = 0; i<SIZE; i++) {
                for(int j = 0; j<SIZE; j++) {
                    if(i == 0 && j == 0) {
                        continue;
                    }
                    cards[i][j] = deck.pop();
                    System.out.println(cards[i][j]);
                }
            }

            System.out.println("Generating population");
            RahulAmanPopulation population = new RahulAmanPopulation(POPULATION_COUNT, INITIAL_MUTATION_RATE, cards);
            System.out.println("Generating population done");
            while(System.currentTimeMillis() - startMillis < TIMEOUT) {
                generations++;

//                System.out.println("Calculating fitness");
                //Calculating fitness of all the children in current population
                population.calculateFitness(pokerSquaresPointSystem);
//                System.out.println("Calculating fitness done");

//                System.out.println("Generating new population");
                //Generates new population after perfroming cross-over and mutation
                population.generate();
//                System.out.println("Generating new population");

                //Get index of best fit child in current population
                int index = population.getBestFit(pokerSquaresPointSystem);

                //Get score of best fit child in current population
                int score = pokerSquaresPointSystem.getScore(population.getChild(index));

                if(verboseLogging) {
                    System.out.println("Generation: " + generations + ": " + score + "   mutation: "+population.getMutationRate());
                }

                //Saving best child from each generation
                if(bestScore < score) {
                    bestScore = score;
                    bestChild = population.getChild(index);
                    bestGeneration = generations;
                    saveBestChild(bestChild);
                }

                //Change mutation rate if evolution is stuck for 500 generations
                if(score <= bestScore && generations % GENERATION_STUCK_LIMIT == 0) {
                    float newMutation = population.getMutationRate() * INCREASE_MUTATION_RATE;
                    if(newMutation > MAX_MUTATION_RATE) {
                        newMutation = INITIAL_MUTATION_RATE;
                    }
                    population.setMutationRate(newMutation);

                }
            }

            if(verboseLogging) {
                System.out.println("Final mutation: " + population.getMutationRate());
                System.out.println("Best generation: " + bestGeneration + ": " + bestScore);
            }
        }

        //Return grid position from best fit child
        Integer rowMajorPos = bestChildMap.get(card);

        if(rowMajorPos == null) {
            rowMajorPos = getUnfilledPosition();
            unfilledPos++;
        }

        unfilledGridPositions.put(rowMajorPos, true);

//        for(int key : unfilledGridPositions.keySet()) {
//            System.out.println("key: "+key + " value: "+unfilledGridPositions.get(key));
//        }

        System.out.println("Card: "+card+ "  value: "+rowMajorPos+" row: "+(rowMajorPos / 5) + " col:"+ rowMajorPos % 5 + " unfilled: "+unfilledPos);
        return new int[]{rowMajorPos / 5, rowMajorPos % 5};
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

            RahulAmanPopulation population = new RahulAmanPopulation(POPULATION_COUNT, INITIAL_MUTATION_RATE, cards);

            while(System.currentTimeMillis() - startMillis < TIMEOUT) {
                generations++;

                //Calculating fitness of all the children in current population
                population.calculateFitness(pokerSquaresPointSystem);

                //Generates new population after perfroming cross-over and mutation
                population.generate();

                //Get index of best fit child in current population
                int index = population.getBestFit(pokerSquaresPointSystem);

                //Get score of best fit child in current population
                int score = pokerSquaresPointSystem.getScore(population.getChild(index));

                if(verboseLogging) {
                    System.out.println("Generation: " + generations + ": " + score + "   mutation: "+population.getMutationRate());
                }

                //Saving best child from each generation
                if(bestScore < score) {
                    bestScore = score;
                    bestChild = population.getChild(index);
                    bestGeneration = generations;
                    saveBestChild(bestChild);
                }

                //Change mutation rate if evolution is stuck for 500 generations
                if(score <= bestScore && generations % GENERATION_STUCK_LIMIT == 0) {
                    float newMutation = population.getMutationRate() * INCREASE_MUTATION_RATE;
                    if(newMutation > MAX_MUTATION_RATE) {
                        newMutation = INITIAL_MUTATION_RATE;
                    }
                    population.setMutationRate(newMutation);

                }
            }

            if(verboseLogging) {
                System.out.println("Final mutation: " + population.getMutationRate());
                System.out.println("Best generation: " + bestGeneration + ": " + bestScore);
            }
        }

        //Return grid position from best fit child
        int rowMajorPos = bestChildMap.get(card);
        return new int[]{rowMajorPos / 5, rowMajorPos % 5};

    }

    /**
     * Save best child from each generation
     * @param child, child or grid to be saved
     */
    private void saveBestChild(Card[][] child) {
        bestChildMap.clear();
        for(int i = 0; i < PokerSquares.SIZE; i++) {
            for(int j = 0; j < PokerSquares.SIZE; j++) {
                bestChildMap.put(child[i][j], i * SIZE + j);
            }
        }
    }


    private int getUnfilledPosition() {
        for(int key : unfilledGridPositions.keySet()) {
//            System.out.println("key: "+key + " value: "+unfilledGridPositions.get(key));
            if(!unfilledGridPositions.get(key)) {
                return key;
            }
        }
        return -1;
    }

    /* (non-Javadoc)
     * @see PokerSquaresPlayer#getName()
     */
    @Override
    public String getName() {
        return "GeneticPlayer";
    }

    /**
     * Demonstrate RahulAmanGeneticPlayer play with British point system.
     * @param args (not used)
     */
    public static void main(String[] args) {
        PokerSquaresPointSystem system = PokerSquaresPointSystem.getBritishPointSystem();
        System.out.println(system);
        RahulAmanGeneticPlayer gp = new RahulAmanGeneticPlayer();
//
        int gameCount = 1;
        int[] scores = new int[gameCount];
        int total = 0;
        for(int i = 0; i<gameCount; i++) {
            scores[i] = new PokerSquares(gp, system).play();
            total += scores[i];
        }
        System.out.println("Average: "+total/scores.length);
//        gp.getUnfilledPosition();
    }

}
