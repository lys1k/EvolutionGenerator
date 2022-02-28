package agh.cs.projektPO.Visualisation;
import agh.cs.projektPO.Classes.Animal;
import agh.cs.projektPO.Classes.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;

public class Statistics {
    private final WorldMap map;
    private double averageNumberOfAnimals = 0;
    private double averageNumberOfGrass = 0;
    private double averageEnergy = 0;
    private double averageLifeSpan = 0;
    private double averageNumbersOfChildren =0;
    private final Map<List<Integer>, Integer> mostCommonGenotype= new HashMap<>();
    private int lifeSpanCounter = 0;

    public Statistics(WorldMap map){
        this.map = map;
    }

    public double getAnimalsEnergy(){
        double animalsEnergy = 0;
        for(Animal animal : map.getAnimalsList()){
            animalsEnergy += animal.getEnergy();
        }
        if(map.getAnimalsList().size() == 0) return 0;
        else {
            return animalsEnergy / map.getAnimalsList().size();
        }
    }

    public double getAverageChildrenNumber(){
        double numOfChildren = 0;
        double numOfParents = 0;
        for(Animal animal: map.getAnimalsList()){
            if(animal.getChildrenCount() > 0){
                numOfChildren += animal.getChildrenCount();
                numOfParents+=1;
            }
        }
        if(numOfParents > 0){
            return numOfChildren/numOfParents;
        }
        return 0;
    }

    public double getNumberOfDeadAnimals(int day){
        int counter = 0;
        for(Animal animal: map.getAnimalsList()){
            if (animal.isDead(day)) {
                counter += 1;
            }
        }
        return counter;
    }

    public double getDeadAnimalsDays(int day){
        int sumOfLifeSpain = 0;
        for(Animal animal: map.getAnimalsList()){
            if (animal.isDead(day)) {
                sumOfLifeSpain += animal.getLengthOfLife();
            }
        }
        return sumOfLifeSpain;
    }

    public void updateStatistics(int day, double lifeSpan){
        this.averageEnergy += this.getAnimalsEnergy();
        if (lifeSpan != 0){
            this.averageLifeSpan += lifeSpan;
            this.lifeSpanCounter += 1;
        }
        this.averageNumberOfAnimals += map.getAnimalsList().size();
        this.averageNumberOfGrass += map.getGrassList().size();
        this.averageNumbersOfChildren += this.getAverageChildrenNumber();

        if(mostCommonGenotype.containsKey(map.getMostCommonGenotype())) {
            int currVal = mostCommonGenotype.get(map.getMostCommonGenotype());
            currVal+=1;
            mostCommonGenotype.remove(map.getMostCommonGenotype());
            mostCommonGenotype.put(map.getMostCommonGenotype(), currVal);
        }
        else{
            mostCommonGenotype.put(map.getMostCommonGenotype(), 1 );
        }
    }

    public List<Integer> getMostCommonGenotype(){
        int maxValue = -1;
        for(Integer value: mostCommonGenotype.values()){
            if(value > maxValue){
                maxValue = value;
            }
        }
        List<Integer> key = new ArrayList<>();
        int value;
        for (Map.Entry<List<Integer>, Integer> elem : mostCommonGenotype.entrySet()) {
            key = elem.getKey();
            value = elem.getValue();
            if(value == maxValue){
                break;
            }
        }
        return key;
    }

    public void outputStatistics(int day) throws IOException {
        day-=1;
        FileWriter myWriter = new FileWriter(String.format("StatisticsAfterDay%s.txt", day));
        myWriter.write(String.format("Statystyki po dniu %s%n", (day)));
        myWriter.write(String.format("Średnia liczba zwierząt: %s%n", (averageNumberOfAnimals/day)));
        myWriter.write(String.format("Średnia liczba roślin: %s%n", (averageNumberOfGrass/day)));
        myWriter.write(String.format("Najczęstszy genotyp %s%n", (getMostCommonGenotype())));
        myWriter.write(String.format("Średni poziom energii %s%n", (averageEnergy/day)));
        myWriter.write(String.format("Średnia długość życia %s%n", (averageLifeSpan/lifeSpanCounter)));
        myWriter.write(String.format("Średnia liczba dzieci %s%n", (averageNumbersOfChildren/day)));
        myWriter.close();
    }
}
