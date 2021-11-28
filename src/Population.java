import java.util.Random;

public class Population {

    private Dna[] population;
    private int populationCount;
    private float mutationRate;

    private Dna bestDna;

    private int generations = 0;

    private double maxFitness;
    private double maxFitnessPopulationIndex;

    public Dna[] getPopulation() {
        return population;
    }

    public int getPopulationCount() {
        return populationCount;
    }

    public int getGenerations() {
        return generations;
    }

    public Population(int populationCount, float mutationRate, Card[][] cards) {
        Dna[] dnas = new Dna[populationCount];
        dnas[0] = new Dna(cards);
        bestDna = dnas[0];
        for(int i = 1; i<dnas.length; i++) {
            dnas[i] = Dna.shuffle(cards);
        }
        this.population = dnas;
        this.populationCount = populationCount;
        this.mutationRate = mutationRate;
    }

    public void calculateFitness(PokerSquaresPointSystem system) {
        for(Dna dna : population) {
            dna.calculateFitness(system);
        }
    }

    public void generate() {
        double maxFitness = 0d;
        for(Dna dna : population) {
            if(maxFitness < dna.getFitness()) {
                maxFitness = dna.getFitness();
                bestDna = dna;
            }
        }

        Dna[] newPopulation = new Dna[population.length];

        for(int i = 0; i<this.population.length; i++) {
            Dna partnerA = acceptReject(maxFitness);
            Dna partnerB = acceptReject(maxFitness);
            Dna child = partnerA.crossOver(partnerB);
            child = child.mutate(mutationRate);
            newPopulation[i] = child;
        }
        this.population = newPopulation;
        generations++;
    }

    public Dna acceptReject(double maxFitness) {
        Random random = new Random();
        while (true) {
            int randomIndex = random.nextInt(this.population.length);
            Dna partnerDna = this.population[randomIndex];
            double randomFitnessMeasure = maxFitness * random.nextDouble();
            if(partnerDna.getFitness() > randomFitnessMeasure) {
                return partnerDna;
            }
        }
    }

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

    public Card[][] getChild(int index) {
        return population[index].getCards();
    }

}
