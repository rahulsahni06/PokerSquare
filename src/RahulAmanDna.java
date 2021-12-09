import java.util.Random;

/**
 * Class denoting one child or grid with fitness scores
 */
public class RahulAmanDna {
    private Card[][] cards;
    private double fitness;

    private static final int MAX_SCORE = 300;

    public Card[][] getCards() {
        return cards;
    }

    public double getFitness() {
        return fitness;
    }

    public RahulAmanDna(Card[][] cards) {
        this.cards = cards;
    }

    /**
     * Calculates fitness of child
     * @param system, PokerSquaresPointSystem
     */
    public void calculateFitness(PokerSquaresPointSystem system) {
        int score = system.getScore(cards);
        fitness = Math.pow(score/((double) MAX_SCORE), 2);
    }

    /**
     * Perform crossover between two parents
     * @param partner, 2nd Parent
     * @return Child after performing crossover
     */
    public RahulAmanDna crossOver(RahulAmanDna partner) {
        Card[][] child = new Card[PokerSquares.SIZE][PokerSquares.SIZE];
        
        for(int i = 0; i < PokerSquares.SIZE; i++) {
            for(int j = 0; j < PokerSquares.SIZE; j++) {
                int random = new Random().nextInt(2);
                if(random == 0) {
                    if (!alreadyPlaced(child, this.cards[i][j]))
                        child[i][j] = this.cards[i][j];
                    else if (!alreadyPlaced(child, partner.cards[i][j]))
                        child[i][j] = partner.cards[i][j];
                     else {
                        // cards from both partners at this position already placed
                        boolean randomCardPlaced = false;
                        while (!randomCardPlaced) {
                            int r1 = new Random().nextInt(5);
                            int r2 = new Random().nextInt(5);
                            Card randomCard = this.cards[r1][r2];
                            if (!alreadyPlaced(child, randomCard)) {
                                child[i][j] = randomCard;
                                randomCardPlaced = true;
                            } 
                        }
                    }
                        
                } else {
                    if (!alreadyPlaced(child, partner.cards[i][j]))
                        child[i][j] = partner.cards[i][j];
                    else if (!alreadyPlaced(child, this.cards[i][j]))
                        child[i][j] = this.cards[i][j];
                     else {
                        // cards from both partners at this position already placed
                        boolean randomCardPlaced = false;
                        while (!randomCardPlaced) {
                            int r1 = new Random().nextInt(5);
                            int r2 = new Random().nextInt(5);
                            Card randomCard = partner.cards[r1][r2];
                            if (!alreadyPlaced(child, randomCard)) {
                                child[i][j] = randomCard;
                                randomCardPlaced = true;
                            }
                        }
                    }
                }
            }
        }

        return new RahulAmanDna(child);
    }

    /**
     * Function to check if card is alread placed or not in the grid.
     * We thought of optimizing it but ran out of time
     * @param child, Grid where we need to check if a particular card is placed or not
     * @param card, card we need to check if it is placed or not
     * @return is Card id placed in the grid or not
     */
    public boolean alreadyPlaced(Card[][] child, Card card) {
        for(int i = 0; i < PokerSquares.SIZE; i++) {
            for(int j = 0; j < PokerSquares.SIZE; j++) {
                if(child[i][j] != null && child[i][j].equals(card)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method mutates a child by shuffling the grid positions
     * @param mutationRate, rate at which mutation will be perfomed
     * @return new mutated child
     */
    public RahulAmanDna mutate(float mutationRate) {
        float random = new Random().nextFloat();
        if(random <= mutationRate) {
            return shuffleLimited(this.cards);
        }
        return this;
    }

    /**
     * Shuffles card in grid
     * Fisher Yates algorithm
     * https://stackoverflow.com/a/26920919/7529668
     * @param cards, grid to shuffle
     * @return new shuffled grid
     */
    public static RahulAmanDna shuffle(Card[][] cards) {
        Card[][] shuffledCards = new Card[cards[0].length][cards.length];
        Random random = new Random();

        for(int i = 0; i<cards.length; i++) {
            System.arraycopy(cards[i], 0, shuffledCards[i],0, shuffledCards[i].length);
        }

        for (int i = cards.length - 1; i > 0; i--) {
            for (int j = cards[i].length - 1; j > 0; j--) {

                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                Card temp = shuffledCards[i][j];
                shuffledCards[i][j] = shuffledCards[m][n];
                shuffledCards[m][n] = temp;

            }
        }
        return new RahulAmanDna(shuffledCards);
    }

    /**
     * Method mutates a child by shuffling the grid positions, it only shuffles part of the grid
     * @return new shuffled grid
     */
    public static RahulAmanDna shuffleLimited(Card[][] cards) {
        Card[][] shuffledCards = new Card[cards[0].length][cards.length];
        Random random = new Random();

        for(int i = 0; i<cards.length; i++) {
            System.arraycopy(cards[i], 0, shuffledCards[i],0, shuffledCards[i].length);
        }

        int maxShuffledElementCount = random.nextInt(23) + 2;
        int shuffledElementCount = 0;

        for (int i = cards.length - 1; i > 0; i--) {
            for (int j = cards[i].length - 1; j > 0; j--) {

                if(shuffledElementCount >= maxShuffledElementCount) {
                    break;
                }

                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                Card temp = shuffledCards[i][j];
                shuffledCards[i][j] = shuffledCards[m][n];
                shuffledCards[m][n] = temp;

                shuffledElementCount++;

            }

            if(shuffledElementCount >= maxShuffledElementCount) {
                break;
            }
        }
        return new RahulAmanDna(shuffledCards);
    }

}
