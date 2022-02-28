package agh.cs.projektPO.Classes;

import java.util.LinkedList;
import java.util.List;

public class DailyActivity {
    private WorldMap map;

    public DailyActivity(WorldMap map){
        this.map = map;
    }

    public void dailyNewGrass(){
        for(int i = 0; i < 5; i++) {
            map.addRandomGrassOnStep(map.getWidth(), map.getHeight());
            map.addRandomGrassOnJungle(map.getWidthJungle(), map.getHeightJungle());
        }
    }

    public void removeDeadAnimals(int day) {
        LinkedList<Animal> deadAnimals = new LinkedList();
        for(Animal animal: map.getAnimalsList()){
            if (animal.isDead(day)) {
                deadAnimals.add(animal);
            }
        }
        for(Animal animal : deadAnimals){
            map.removeAnimal(animal, animal.getPosition());
            animal.removeObserver(map);
            map.getAnimalsList().remove(animal);
        }
    }

    public void decreaseDayEnergy() {
        for (LinkedList<Animal> animalList : map.getAnimals().values()) {
            if (animalList != null) {
                if (animalList.size() > 0) {
                    for (Animal a : animalList) {
                        a.changeEnergy(-map.getDayCost());
                    }
                }
            }
        }
    }

    public void copulation(int day){
        LinkedList<Animal> tmp = new LinkedList<>();
        for(LinkedList<Animal> animals : map.getAnimals().values()){
            if(animals != null){
                if(animals.size() > 1) {
                    animals.sort((o1, o2) -> o2.getEnergy() - o1.getEnergy());
                    Animal father = animals.get(0);
                    Animal mother = animals.get(1);
                    if (mother.getEnergy() > map.getStartEnergy() / 2 && father.getEnergy() > map.getStartEnergy() / 2) {
                        Animal child = father.copulate(mother, father.getPosition(),day);
                        Vector2d childPosition = map.getRandomPositionSurrounding(mother.getPosition());
                        if (childPosition != null || child != null) {
                            childPosition = map.normalisePosition(childPosition);
                            child.fillChildPosition(childPosition);
                            List genes = child.getGenes().getListGenes();
                            if(map.getGenotypeMap().containsKey(genes)) {
                                int currVal = map.getGenotypeMap().get(genes);
                                map.getGenotypeMap().remove(child);
                                currVal+=1;
                                map.getGenotypeMap().put(genes, currVal);
                            }
                            else{
                                map.getGenotypeMap().put(genes, 1);
                            }
                            tmp.add(child);
                        }
                    }
                }
            }
        }
        for(Animal animal: tmp){
            map.addAnimal(animal, day);
        }
    }

    public void consumption(){
        LinkedList<Grass> grassToRemove = new LinkedList<>();
        for(Grass gras : map.getGrass().values()){
            LinkedList<Animal> animals_eat = map.getAnimals().get(gras.getPosition());
            if(animals_eat != null) {
                if(animals_eat.size() >0) {
                    animals_eat.sort((o1, o2) -> o2.getEnergy() - o1.getEnergy());
                    int animalEnergy = animals_eat.getFirst().getEnergy();
                    int numOfAnimals = 0;
                    LinkedList<Animal> animalsWhoCanEat = new LinkedList<>();
                    for (Animal animal : animals_eat) {
                        if (animal.getEnergy() == animalEnergy) {
                            animalsWhoCanEat.add(animal);
                            numOfAnimals += 1;
                        } else {
                            break;
                        }
                    }
                    for (Animal animal2 : animalsWhoCanEat) {
                        animal2.changeEnergy(map.getGrassProfit() / numOfAnimals);
                    }
                    grassToRemove.add(gras);
                }
            }
        }
        for(Grass gras : grassToRemove){
            map.removeGrass(gras);
        }
    }
}
