import java.util.Random;

/**
 * Class denoting the population or collection of all the possible grids
 */
public class RahulAmanPopulation {

    private RahulAmanDna[] population;
    private float mutationRate;

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }
    public float getMutationRate() {
        return mutationRate;
    }


    public RahulAmanPopulation(int populationCount, float mutationRate, Card[][] cards) {
        RahulAmanDna[] dnas = new RahulAmanDna[populationCount];
        dnas[0] = new RahulAmanDna(cards);
        for(int i = 1; i<dnas.length; i++) {
            dnas[i] = RahulAmanDna.shuffle(cards);
        }
        this.population = dnas;
        this.mutationRate = mutationRate;
    }

    /**
     * Function to calcute fitness of all the children in population
     * @param system, PokerSquaresPointSystem to help calculate the fitness
     */
    public void calculateFitness(PokerSquaresPointSystem system) {
        for(RahulAmanDna dna : population) {
            dna.calculateFitness(system);
        }
    }

    /**
     * Generates new population by picking parents and performing cross-over
     * and mutation
     */
    public void generate() {
        double maxFitness = 0d;
        for(RahulAmanDna dna : population) {
            if(maxFitness < dna.getFitness()) {
                maxFitness = dna.getFitness();
            }
        }

        RahulAmanDna[] newPopulation = new RahulAmanDna[population.length];

        for(int i = 0; i<this.population.length; i++) {
            RahulAmanDna partnerA = acceptReject(maxFitness);
            RahulAmanDna partnerB = acceptReject(maxFitness);
            RahulAmanDna child = partnerA.crossOver(partnerB);
            child = child.mutate(mutationRate);
            newPopulation[i] = child;
        }
        this.population = newPopulation;
    }

    /**
     * This methods helps in picking a parent according to its fitness
     * @param maxFitness, fitness score of best child in current population
     * @return Parent
     */
    public RahulAmanDna acceptReject(double maxFitness) {
        Random random = new Random();
        int i = 0;
        while (i < 30000) {
            int randomIndex = random.nextInt(this.population.length);
            RahulAmanDna partnerDna = this.population[randomIndex];
            double randomFitnessMeasure = maxFitness * random.nextDouble();
            if(partnerDna.getFitness() > randomFitnessMeasure) {
                return partnerDna;
            }
            i++;
        }
        int randomIndex = random.nextInt(this.population.length);
        return this.population[randomIndex];
    }

    /**
     * Method to find index of best child in the population array
     * @param system, PokerSquaresPointSystem
     * @return returns index of best child
     */
    public int getBestFit(PokerSquaresPointSystem system) {
        int index = 0;
        int maxScore = 0;
        for(int i = 0; i<population.length; i++) {
            int score = system.getScore(population[i].getCards());
            if(maxScore < score) {
                maxScore = score;
                index = i;
            }
        }
        return index;
    }

    /**
     * Method to get child at a particular index
     * @param index, index in population array
     * @return Child
     */
    public Card[][] getChild(int index) {
        return population[index].getCards();
    }

}
