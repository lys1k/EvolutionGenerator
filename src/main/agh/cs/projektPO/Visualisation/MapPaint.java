package agh.cs.projektPO.Visualisation;
import agh.cs.projektPO.Classes.Animal;
import agh.cs.projektPO.Classes.Grass;
import agh.cs.projektPO.Classes.WorldMap;
import agh.cs.projektPO.Classes.Vector2d;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MapPaint extends JPanel {
    private final Simulation sim;
    private final WorldMap map;

    public MapPaint(Simulation sim, WorldMap map){
        this.map = map;
        this.sim = sim;

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int multi = sim.getMulti();

        Color fillColor = new Color(199,255,59);
        g.setColor(fillColor);
        g.fillRect(0,0,sim.getWidth()*multi,sim.getHeight()*multi);

        int jungleHeight =sim.getJungleHeight(sim.getWidth(), sim.getHeight(), sim.getJungleRatio());
        int jungleWidth = sim.getJungleWidth(sim.getWidth(), sim.getHeight(), sim.getJungleRatio());
        Vector2d vector= sim.getLowerLeftJungle(sim.getWidth(), sim.getHeight(), sim.getJungleRatio());

        Color jungleColor = new Color(38, 110, 4);
        Color energy80 = new Color(51, 29, 1);
        Color energy60 = new Color(79, 45, 2);
        Color energy40 = new Color(107, 60, 1);
        Color energy20 = new Color(150, 84, 2);
        Color energy0 = new Color(224, 156, 72);

        g.setColor(jungleColor);
        g.fillRect(vector.getX()*multi, vector.getY()*multi,jungleWidth*multi, jungleHeight*multi);

        g.setColor(Color.GREEN);
        for(Grass grass : map.getGrassList()){
            Vector2d position = grass.getPosition();
            g.fillOval(position.getX()*multi, position.getY()*multi, multi, multi);
        }

        for(Animal animal : map.getAnimalsList()){
            if(sim.getShowMostCommonGenotype() && Arrays.equals(animal.getGenes().getListGenes().toArray(), map.getMostCommonGenotype().toArray())){
                g.setColor(Color.RED);
            }
            else if (animal.getEnergy() > 0.8*sim.getStartEnergy()){
                g.setColor(energy80);
            } else if (animal.getEnergy() > 0.6*sim.getStartEnergy()){
                g.setColor(energy60);
            } else if (animal.getEnergy() > 0.4*sim.getStartEnergy()){
                g.setColor(energy40);
            } else if (animal.getEnergy() > 0.2*sim.getStartEnergy()){
                g.setColor(energy20);
            } else {
                g.setColor(energy0);
            }

            Vector2d position  = animal.getPosition();
            g.fillOval(position.getX()*multi, position.getY()*multi, multi, multi);
        }
    }
}
