package agh.cs.projektPO.Classes;
import agh.cs.projektPO.Enums.MapDirection;
import agh.cs.projektPO.Interfaces.IMapElement;
import agh.cs.projektPO.Interfaces.IPositionChangeObserver;
import agh.cs.projektPO.Interfaces.IWorldMap;

import java.util.*;

public class WorldMap implements IWorldMap, IPositionChangeObserver {
    private int grassProfit;
    private int dayCost;
    private int startEnergy;

    private final Map<Vector2d, LinkedList<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grass = new HashMap<>();
    private final LinkedList<Animal> animalsList = new LinkedList<>();
    private final LinkedList<Grass> grassList = new LinkedList<>();
    private final Map<List<Integer>, Integer> genotype= new HashMap<>();

    private final Vector2d upperRight;
    private final Vector2d lowerLeft;
    private final Vector2d upperRightJungle;
    private final Vector2d lowerLeftJungle;

    public WorldMap(Vector2d lowerLeft, Vector2d upperRight, Vector2d lowerLeftJungle, Vector2d upperRightJungle){
        this.lowerLeft = lowerLeft;
        this.upperRight = upperRight;
        this.lowerLeftJungle = lowerLeftJungle;
        this.upperRightJungle = upperRightJungle;
    }

    public void setGrassProfit(int grassProfit){
        this.grassProfit = grassProfit;
    }

    public void setStartEnergy(int startEnergy) {
        this.startEnergy = startEnergy;
    }

    public void setDayCost(int dayCost){
        this.dayCost = dayCost;
    }

    public int getDayCost(){
        return dayCost;
    }

    public int getStartEnergy(){
        return startEnergy;
    }

    public int getGrassProfit() {
        return grassProfit;
    }

    public int getWidth() {
        return this.upperRight.x - this.lowerLeft.x + 1;
    }

    public int getHeight() {
        return this.upperRight.y - this.lowerLeft.y + 1;
    }

    public int getWidthJungle() {
        return this.upperRightJungle.x - this.lowerLeftJungle.x ;
    }

    public int getHeightJungle() {
        return this.upperRightJungle.y - this.lowerLeftJungle.y ;
    }

    public LinkedList<Animal> getAnimalsList(){
        return animalsList;
    }

    public  LinkedList<Grass> getGrassList(){
        return grassList;
    }

    public Map<Vector2d, LinkedList<Animal>> getAnimals(){
        return animals;
    }

    public Map<Vector2d,Grass> getGrass(){
        return grass;
    }

    public Map<List<Integer>, Integer> getGenotypeMap(){
        return genotype;
    }

    public void addAnimal(Animal animal, int day) {
        animal.setBirthDay(day);
        Vector2d position = normalisePosition(animal.getPosition());
        if (!animals.containsKey(position)) {
            LinkedList<Animal> toAddList = new LinkedList<>();
            toAddList.add(animal);
            animalsList.add(animal);
            animals.put(position,toAddList);
            animal.addObserver(this);
        }
        else {
            LinkedList<Animal> toAddList = animals.get(position);
            toAddList.add(animal);
            animalsList.add(animal);
        }
        List genes  = animal.getGenes().getListGenes();
        if(genotype.containsKey(genes)){
            int currVal = genotype.get(genes);
            currVal+=1;
            genotype.remove(genes);
            genotype.put(genes, currVal);
        }
        else{
            genotype.put(genes, 1);
        }
    }

    public void removeAnimal(Animal animal, Vector2d pos) {
        Vector2d position = normalisePosition(pos);
        LinkedList<Animal> toCheck = animals.get(position);
        if(toCheck != null) {
            toCheck.remove(animal);
            animalsList.remove(animal);
            animal.removeObserver(this);
            if (animals.get(position).isEmpty()) {
                animals.remove(position);
                int value = genotype.get(animal.getGenes().getListGenes());
                value -= 1;
                genotype.remove(animal.getGenes().getListGenes());
                if (value > 0) {
                    genotype.put(animal.getGenes().getListGenes(), value);
                }
            }
        }
    }

    public void addAnimalOnRandomPlace(int width, int height, int day){
        Vector2d position = getRandomPosition(width, height);
        if (position != null){
            List<Integer> genes = new ArrayList<>(32);
            Genes gene = new Genes(genes);
            gene.fillGenes();
            Animal animal = new Animal(this,position, startEnergy, gene);
            animal.setBirthDay(day);
            animal.direction = animal.direction.getRandomDirection();
            addAnimal(animal, day);
            if(genotype.containsKey(genes)){
                int currVal = genotype.get(genes);
                currVal+=1;
                genotype.remove(genes);
                genotype.put(genes, currVal);
            }
            else{
                genotype.put(genes, 1);
            }
        }
    }

    public void addGrass(Grass element){
        Vector2d position = element.getPosition();
            if(grass.get(position) == null){
                grass.put(position, element);
                grassList.add(element);
            }
    }

    public void removeGrass(Grass gras){
        grass.remove(gras.getPosition());
        grassList.remove(gras);
    }

    @Override
    public Object objectAt (Vector2d positionObject){
        Vector2d position = normalisePosition(positionObject);
        LinkedList<Animal> toCheck = animals.get(position);
        if(toCheck == null)  return grass.get(position);
        if(toCheck.size() == 0) return grass.get(position);
        return toCheck.getFirst();
    }

    @Override
    public boolean isOccupied(Vector2d position){
        return objectAt(position) != null;
    }

    @Override
    public boolean canMoveTo(Vector2d positionToCheck){
        Vector2d position = normalisePosition(positionToCheck);
        if(animals.get(position) == null) {
            return true;
        }
        else return animals.get(position).size() <= 10;
    }

    @Override
    public void onPositionChange(Animal animal, Vector2d oldPos) {
        Vector2d oldPosition = normalisePosition(oldPos);
        Vector2d newPosition = normalisePosition(animal.getPosition());
        animals.get(oldPosition).remove(animal);
        if (animals.get(oldPosition).isEmpty()) {
            animals.remove(oldPosition);
        }
        if (!animals.containsKey(newPosition)) {
            animals.put(newPosition, new LinkedList<>());
        }
        animals.get(newPosition).add(animal);
        animal.setPosition(newPosition);
    }

    public Vector2d normalisePosition(Vector2d position) {
        int newX, newY;
        if (position.x >= 0) {
            newX = position.x % (upperRight.x + 1);
        } else {
            newX = (upperRight.x + (1 + position.x) % (upperRight.x + 1));
        }
        if (position.y >= 0) {
            newY = position.y % (upperRight.y + 1);
        } else {
            newY = (upperRight.y + (1 + position.y) % (upperRight.y + 1));
        }
        return new Vector2d(newX, newY);
    }

    public Animal getAnimalByPosition(Vector2d position){
        LinkedList<Animal> animalsAtPosition;
        animalsAtPosition = animals.get(position);
        if(animalsAtPosition != null) {
            if(animalsAtPosition.size() > 0) {
                animalsAtPosition.sort((o1, o2) -> o2.getEnergy() - o1.getEnergy());
            }
            return animals.get(position).getFirst();
        }
        return null;
    }

    public Vector2d getRandomPositionSurrounding(Vector2d position) {
        for (MapDirection direction : MapDirection.values()) {
            Vector2d newPosition = position.add(direction.toUnitVector());
            if (!(animals.containsKey(newPosition) || grass.containsKey(newPosition))){
                if(newPosition.follows(lowerLeft) && newPosition.precedes(upperRight)){
                    return position.add(direction.toUnitVector());
                }
            }
        }
        return getRandomPosition(getWidth(), getHeight());
    }

    public Vector2d getRandomPosition(int width, int height) {
        LinkedList<Vector2d> toCheck = new LinkedList<>();
        Vector2d position = new Vector2d((int) (Math.random() * (width)), (int) (Math.random() * (height)));

        while (toCheck.size() < width * height) {
            if (toCheck.size() == width * height) break;
            if (this.objectAt(position) == null) {
                return position;
            }
            else {
                position = new Vector2d((int) (Math.random() * (width)), (int) (Math.random() * (height)));
                if (!toCheck.contains(position)) {
                    toCheck.add(position);
                }
            }
        }
        return null;
    }

    public void addRandomGrassOnJungle(int width, int height) {
        Vector2d position =  new Vector2d((int) (Math.random() * (width )), (int) (Math.random() * (height)));
        position = position.add(lowerLeftJungle);
        LinkedList<Vector2d> toCheck = new LinkedList<>(); //Lista tworzona aby moc sprawdzić czy nie sprawdziłam wszystkich mozliwych pozycji
        toCheck.add(position);
        boolean flag = true;
        while(flag && toCheck.size() < width*height) {
            if(toCheck.size() == width * height) break;
            if (this.objectAt(position) == null) {
                addGrass(new Grass(position));
                flag = false;
            }
            else{
                position =  new Vector2d((int) (Math.random() * (width)), (int) (Math.random() * (height)));
                position = position.add(lowerLeftJungle);
                if (!toCheck.contains(position)){
                    toCheck.add(position);
                }
            }
        }
    }

    public void addRandomGrassOnStep(int width, int height){
        Vector2d position;
        boolean flag = true;
        while(flag){
            position = getRandomPosition(width, height);
            if(position== null) flag = false; //jesli metoda getRandomPosition zwroci null to znaczy ze skonczyly sie wolne miejsca
            else if((position.follows(lowerLeftJungle)) && (position.precedes(upperRightJungle))){
                flag = true;
            }
            else{
                addGrass(new Grass(position));
                flag = false;
            }
        }
    }

    public List<Integer> getMostCommonGenotype(){
        int maxValue = -1;
        for(Integer value: genotype.values()){ //szukanie najczesciej wystepujacego genotypu
            if(value > maxValue){
                maxValue = value;
            }
        }
        List<Integer> key = new ArrayList<>();
        int value;
        for (Map.Entry<List<Integer>, Integer> elem : genotype.entrySet()) { //sprawdzanie jaki zwierzak ma najczesciej wystepujacy genotyp
            key = elem.getKey();
            value = elem.getValue();
            if(value == maxValue){
                break;
            }
        }
        return key;
    }
}
