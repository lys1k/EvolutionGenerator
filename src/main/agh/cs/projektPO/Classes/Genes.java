package agh.cs.projektPO.Classes;

import agh.cs.projektPO.Enums.MapDirection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Genes {

    public List<Integer> genes = new ArrayList<>(32);
    private final int numOfGenes=8;
    private final int size = 32;
    private int[] genesCounter = new int[numOfGenes];

    public Genes(List<Integer> genes){
        this.genes = genes;
        for(Integer gene: genes){
            genesCounter[gene]++;
        }
    }

    public void fillGenes(){
        for(int i =0; i<numOfGenes; i++){
            genes.add(i);
            genesCounter[i]+=1;
        }
        for(int i = numOfGenes; i< size; i++){
            int random = (int) (Math.random() * (numOfGenes));
            genes.add(random);
            genesCounter[random]+=1;
        }
        genes.sort(Integer::compareTo);
    }

    public List<Integer> getListGenes(){
        return genes;
    }

    public Genes(Genes mother, Genes father){
        int size = 32;
        int div1 = (int)(Math.random()*(size-1));
            int div2 = div1;
            int[] tempGeneCount = new int[numOfGenes];

            while(div2 == div1){
                div2 = (int)(Math.random()*(size));
            }
            if(div2 <  div1) {
                int tmp = div1;
                div1 = div2;
                div2 = tmp;
            }

            for(int i = 0; i < size; i++ ){
                if(i <=div1 || i > div2){
                    tempGeneCount[father.genes.get(i)]+=1;
                }
                else{
                    tempGeneCount[mother.genes.get(i)]+=1;
                }
            }
            for (int i = 0; i < numOfGenes; i++) {

                if (tempGeneCount[i] == 0) {
                    int random = (int) (Math.random() * (numOfGenes-1));
                    if (tempGeneCount[random] > 1) {

                        tempGeneCount[random] -= 1;
                        tempGeneCount[i] += 1;
                    }
                    else{
                        i -= 1;
                    }
                }
            }
        genesCounter = Arrays.copyOf(tempGeneCount, numOfGenes);

        for(int i = 0; i <numOfGenes; i++){
            while(tempGeneCount[i] > 0){
                tempGeneCount[i] -=1;
                genes.add(i);
            }
        }
    }

    public int[] getGeneCounter(){
        return genesCounter;
    }

    public int getRandomGene(){
        int random = (int) (Math.random() * (size - 1));
        return genes.get(random);
    }

    public String toString(){
        return Arrays.toString(genesCounter);
    }

    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Genes)){
            return false;
        }
        Genes genes = (Genes) other;
        return Arrays.equals(genesCounter, genes.genesCounter);
    }
  
    @Override
    public int hashCode() {
        return Arrays.hashCode(genesCounter);
    }

}
