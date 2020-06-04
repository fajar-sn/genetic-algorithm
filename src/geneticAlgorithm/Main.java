package geneticAlgorithm;

import java.util.Random;

public class Main {
    
    public static void main(String[] args) {
        int numberOfCities = 4;
        int[][] travelPrices = new int[numberOfCities][numberOfCities];
        for(int i = 0; i < numberOfCities; i++) {
            for(int j = 0; j <= i; j++) {
                Random random = new Random();
                if(i == j)
                    travelPrices[i][j] = 0;
                else {
                    travelPrices[i][j] = random.nextInt(100);
                    travelPrices[j][i] = travelPrices[i][j];
                }
            }
        }
        printTravelPrices(travelPrices, numberOfCities);
        TravelingSalesman travelingSalesman = new TravelingSalesman(numberOfCities, SelectionType.ROULETTE, travelPrices, 0, 0);
        SalesmanGenome result = travelingSalesman.optimize();
        System.out.println("\nHasil:");
        System.out.println(result);
    }
    
    public static void printTravelPrices(int[][] travelPrices, int numberofCities) {
        for(int i = 0; i < numberofCities; i++) {
            for(int j = 0; j < numberofCities; j++)
                System.out.println("Biaya perjalanan dari kota " + i + " ke kota " + j + " = " + travelPrices[i][j]);
        }
    }
}
