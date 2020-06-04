package geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesmanGenome {
    private List<Integer> genome;
    private int[][] travelPrices;
    private int startingCity;
    private int numberOfCities;
    private int fitness;

    public SalesmanGenome(int numberOfCities, int[][] travelPrices, int startingCity) {
        this.numberOfCities = numberOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.genome = randomSalesman();
        this.fitness = calculateFitness();
    }

    public SalesmanGenome(List<Integer> permutationOfCities, int[][] travelPrices, int startingCity, int numberOfCities) {
        this.genome = permutationOfCities;
        this.travelPrices = travelPrices;
        this.startingCity = startingCity;
        this.numberOfCities = numberOfCities;
        this.fitness = calculateFitness();
    }
    
    private List<Integer> randomSalesman() {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < numberOfCities; i++) {
            if(i != startingCity)
                result.add(i);
        }
        Collections.shuffle(result);
        return result;
    }

    private int calculateFitness() {
        int fitness = 0;
        int currentCity = startingCity;
        for(int gene : genome) {
            fitness += travelPrices[currentCity][gene];
            currentCity = gene;
        }
        fitness += travelPrices[genome.get(numberOfCities - 2)][startingCity];
        return fitness;
    }

    public List<Integer> getGenome() {
        return genome;
    }

    public int getStartingCity() {
        return startingCity;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Rute: ");
        stringBuilder.append(startingCity);
        for(int gene : genome) {
            stringBuilder.append(" ");
            stringBuilder.append(gene);
        }
        stringBuilder.append(" ");
        stringBuilder.append(startingCity);
        stringBuilder.append("\tJarak: ");
        stringBuilder.append(this.fitness);
        return stringBuilder.toString();
    }
    
    public int compareTo(Object object) {
        SalesmanGenome genome = (SalesmanGenome) object;
        if(fitness > genome.getFitness())
            return 1;
        else if(fitness < genome.getFitness())
            return -1;
        else
            return 0;
    }
}
