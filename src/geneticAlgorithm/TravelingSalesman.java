package geneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class TravelingSalesman {
    private int generationSize;
    private int genomeSize;
    private int numberOfCities;
    private int reproductionSize;
    private int maxIterations;
    private int startingCity;
    private int targetFitness;
    private int tournamentSize;
    private int[][] travelPrices;
    private float mutationRate;
    private SelectionType selectionType;

    public TravelingSalesman(int numberOfCities, SelectionType selectionType, int[][] travelPrices, int startingCity, int targetFitness) {
        this.numberOfCities = numberOfCities;
        this.genomeSize = numberOfCities - 1;
        this.startingCity = startingCity;
        this.targetFitness = targetFitness;
        this.travelPrices = travelPrices;
        this.selectionType = selectionType;
        generationSize = 100;
        reproductionSize = 200;
        maxIterations = 100;
        mutationRate = 0.1f;
        tournamentSize = 40;
    }
    
    public List<SalesmanGenome> initialPopulation() {
        List<SalesmanGenome> population = new ArrayList<>();
        for(int i = 0; i < generationSize; i++)
            population.add(new SalesmanGenome(numberOfCities, travelPrices, startingCity));
        return population;
    }
    
    public List<SalesmanGenome> selection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = new ArrayList<>();
        SalesmanGenome winner;
        for (int i=0; i < reproductionSize; i++) {
            if (selectionType == SelectionType.ROULETTE) {
                selected.add(rouletteSelection(population));
            }
            else if (selectionType == SelectionType.TOURNAMENT) {
                selected.add(tournamentSelection(population));
            }
        }
        return selected;
    }
    
    public SalesmanGenome rouletteSelection(List<SalesmanGenome> population) {
        int totalFitness = population.stream().map(SalesmanGenome::getFitness).mapToInt(Integer::intValue).sum();
        Random random = new Random();
        int selectedValue = random.nextInt(totalFitness);
        float recValue = (float) 1 / selectedValue;
        float currentSum = 0;
        for(SalesmanGenome genome : population) {
            currentSum += (float) 1 / genome.getFitness();
            if(currentSum >= recValue)
                return genome;
        }
        int selectRandom = random.nextInt(generationSize);
        return population.get(selectRandom);
    }
    
    public SalesmanGenome tournamentSelection(List<SalesmanGenome> population) {
        List<SalesmanGenome> selected = pickNRandomElements(population, tournamentSize);
        Map<SalesmanGenome, Integer> genomeFitness = new HashMap<>();
        for (SalesmanGenome salesmanGenome : selected) {
            genomeFitness.put(salesmanGenome, salesmanGenome.getFitness());
        }
        return Collections.min(genomeFitness.entrySet(), Entry.comparingByValue()).getKey();
    }
    
    public static <E> List<E> pickNRandomElements(List<E> list, int n) {
        Random random = new Random();
        int length = list.size();
        if(length < n)
            return null;
        for(int i = length - 1; i >= length - n; --i)
            Collections.swap(list, i, random.nextInt(i + 1));
        return list.subList(length - n, length);
    }
    
    public List<SalesmanGenome> crossover(List<SalesmanGenome> parents) {
        Random random = new Random();
        int breakpoint = random.nextInt(genomeSize);
        List<SalesmanGenome> children = new ArrayList<>();
        List<Integer> parent1Genome = new ArrayList<>(parents.get(0).getGenome());
        List<Integer> parent2Genome = new ArrayList<>(parents.get(1).getGenome());
        int i;
        int newValue;
        for(i = 0; i < breakpoint; i++) {
            newValue = parent2Genome.get(i);
            Collections.swap(parent1Genome, parent1Genome.indexOf(newValue), i);
        }
        children.add(new SalesmanGenome(parent1Genome, travelPrices, startingCity, numberOfCities));
        parent1Genome = parents.get(0).getGenome();
        for(i = breakpoint; i < genomeSize; i++) {
            newValue = parent1Genome.get(i);
            Collections.swap(parent2Genome, parent2Genome.indexOf(newValue), i);
        }
        children.add(new SalesmanGenome(parent2Genome, travelPrices, startingCity, numberOfCities));
        return children;
    }
    
    public SalesmanGenome mutate(SalesmanGenome salesman) {
        Random random = new Random();
        float mutate = random.nextFloat();
        if(mutate < mutationRate) {
            List<Integer> genome = salesman.getGenome();
            Collections.swap(genome, random.nextInt(genomeSize), random.nextInt(genomeSize));
            return new SalesmanGenome(genome, travelPrices, startingCity, numberOfCities);
        }
        return salesman;
    }
    
    public List<SalesmanGenome> createGeneration(List<SalesmanGenome> population) {
        List<SalesmanGenome> generation = new ArrayList<>();
        int currentGenerationSize = 0;
        while(currentGenerationSize < generationSize) {
            List<SalesmanGenome> parents = pickNRandomElements(population, 2);
            List<SalesmanGenome> children = crossover(parents);
            children.set(0, mutate(children.get(0)));
            children.set(1, mutate(children.get(1)));
            generation.addAll(children);
            currentGenerationSize += 2;
        }
        return generation;
    }
    
    public SalesmanGenome optimize() {
        List<SalesmanGenome> population = initialPopulation();
        printGeneration(population);
        SalesmanGenome globalBestGenome = population.get(0);
        for(int i = 0; i < maxIterations; i++) {
            List<SalesmanGenome> selected = selection(population);
            population = createGeneration(selected);
            Map<SalesmanGenome, Integer> genomeFitness = new HashMap<>();
            for (SalesmanGenome salesmanGenome : population) {
                genomeFitness.put(salesmanGenome, salesmanGenome.getFitness());
            }
            globalBestGenome = Collections.min(genomeFitness.entrySet(), Entry.comparingByValue()).getKey();
            if(globalBestGenome.getFitness() < targetFitness)
                break;
        }
        return globalBestGenome;
    }
    
    public void printGeneration(List<SalesmanGenome> generation) {
        for(SalesmanGenome genome : generation)
            System.out.println(genome);
    }
}
