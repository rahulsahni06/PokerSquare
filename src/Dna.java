import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class Dna {
    private Card[][] cards;
    private double fitness;

    public Card[][] getCards() {
        return cards;
    }

    public double getFitness() {
        return fitness;
    }

    public Dna(Card[][] cards) {
        this.cards = cards;
    }

    public void calculateFitness(PokerSquaresPointSystem system) {
        int score = system.getScore(cards);
        fitness = Math.pow(score/128d, 2);
    }

    public Dna crossOver(Dna partner) {
        Card[][] child = new Card[PokerSquares.SIZE][PokerSquares.SIZE];
        int random = new Random().nextInt(2);

        HashMap<String, Integer> map = new HashMap<>();

        for(int i = 0; i<PokerSquares.SIZE; i++) {
            for(int j = 0; j<PokerSquares.SIZE; j++) {
//                map.put(this.cards[i][j].toString(), i*PokerSquares.SIZE+j);
//                map.put(this.cards[i][j].toString(), i*PokerSquares.SIZE+j);
                if(random == 0) {
                    child[i][j] = this.cards[i][j];
                } else {
                    child[i][j] = partner.cards[i][j];
                }

            }
        }
        return new Dna(child);
    }

    public Dna mutate(float mutationRate) {
        float random = new Random().nextFloat();
        if(random <= mutationRate) {
            return shuffle(this.cards);
        }
        return this;
    }

//    Fisher–Yates algorithm
//    https://stackoverflow.com/a/26920919/7529668
    public static Dna shuffle(Card[][] cards) {
        Card[][] shuffledCards = new Card[cards[0].length][cards.length];
        Random random = new Random();

        for(int i = 0; i<cards.length; i++) {
            System.arraycopy(cards[i], 0, shuffledCards[i],0, shuffledCards[i].length);
        }

        for (int i = cards.length - 1; i > 0; i--) {
            for (int j = cards[i].length - 1; j > 0; j--) {

//                shuffledCards[i][j] = cards[i][j];
                int m = random.nextInt(i + 1);
                int n = random.nextInt(j + 1);

                Card temp = shuffledCards[i][j];
                shuffledCards[i][j] = shuffledCards[m][n];
                shuffledCards[m][n] = temp;

//                shuffledCards[i][j] = cards[m][n];
//                shuffledCards[m][n] = cards[i][j];
            }
        }
        return new Dna(shuffledCards);
    }

}
