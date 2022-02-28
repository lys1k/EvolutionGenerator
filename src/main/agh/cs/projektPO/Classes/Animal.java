package agh.cs.projektPO.Classes;
import agh.cs.projektPO.Enums.MapDirection;
import agh.cs.projektPO.Interfaces.IMapElement;
import agh.cs.projektPO.Interfaces.IPositionChangeObserver;
import agh.cs.projektPO.Interfaces.IWorldMap;
import java.util.*;

public class Animal implements IMapElement {
    public MapDirection direction = MapDirection.NORTH;
    private Vector2d position;
    private final IWorldMap map;
    private final List<IPositionChangeObserver> observers = new ArrayList<>();
    private final Set<Animal> children = new HashSet<>();
    private int energy;
    private Genes genes;
    int birthDay;
    int deathDay;

    public Animal(IWorldMap map, Vector2d initialPosition){
        this.map = map;
        this.position = initialPosition;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int energy, Genes genes){
        this(map, initialPosition);
        this.genes = genes;
        this.energy = energy;
    }

    public Animal(IWorldMap map, Vector2d initialPosition, int energy){
        this(map, initialPosition);
        this.energy = energy;
    }

    public IWorldMap getMap(){
        return this.map;}

    public void changeEnergy(int value){
        this.energy = this.energy + value;
    }

    public boolean isDead(int day){
        if(energy <=0){
            deathDay = day;
            return true;
        }
        return false;
    }

    public boolean isDead(){
        if(energy <=0){
            return true;
        }
        return false;

    }

    public void setBirthDay(int birthDay){
        this.birthDay = birthDay;
    }

    public void setDeathDay(int deadthDay){
        this.deathDay = deadthDay;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public void setPosition(Vector2d pos){
        this.position = pos;
    }

    public Genes getGenes(){
        return genes;
    }

    public int getBirthDay(){
        return this.birthDay;
    }

    public int getEnergy(){
        return energy;
    }

    public int getChildrenCount(){
        return this.children.size();
    }

    @Override
    public String toString() {
       if (isDead()){
           return "X";
        }
       return direction.toString();
    }

    public void move() {
        this.rotate();
        Vector2d oldPosition = this.position;
        this.position = this.position.add(this.direction.toUnitVector());
        notifyPositionChange(oldPosition);
    }

    public void addChild(Animal child){
        this.children.add(child);
    }

    public int getLengthOfLife(){
        if(this.isDead()){
            return deathDay - birthDay;
        }
        return 0;
    }

    public void rotate(){
         int numOfRotation = genes.getRandomGene();
         for(int i = 0; i< numOfRotation; i++){
          this.direction = this.direction.next();
        }
    }

    public void setGenes(Genes genes){
        this.genes = genes;
    }

    public  Animal copulate(Animal animal, Vector2d position, int birthDay){
        Animal child = null;

        int EnergyForChild = (int)(0.25 * this.energy);
        int EnergyForChild2 = (int)(0.25 * animal.energy);
        int childEnergy = (EnergyForChild + EnergyForChild2);
        this.changeEnergy(-EnergyForChild);
        animal.changeEnergy(-EnergyForChild2);

        Genes genesForChild = new Genes(this.getGenes(), animal.getGenes());

        child = new Animal(map, position, childEnergy, genesForChild); //zmienic pozycje
        animal.addChild(child);
        this.addChild(child);
        return child;
    }

    public void fillChildPosition(Vector2d position){
        this.position = position;
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    private void notifyPositionChange(Vector2d oldPosition) {
        observers.forEach(o -> o.onPositionChange(this, oldPosition));
    }

    public int getFamily(LinkedList<Animal> toCheckDuplicate){
        int counter = this.getChildrenCount();
        for(Animal child : children){
            if (!(toCheckDuplicate.contains(child))){
                child.getFamily(toCheckDuplicate);
                counter += child.getChildrenCount();
                toCheckDuplicate.add(child);
            }
        }
        return counter;
    }
}




