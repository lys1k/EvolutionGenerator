package agh.cs.projektPO.Visualisation;
import agh.cs.projektPO.Classes.Animal;
import agh.cs.projektPO.Classes.DailyActivity;
import agh.cs.projektPO.Classes.WorldMap;
import agh.cs.projektPO.Classes.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.Timer;

public class Simulation {
    private final int numOfAnimals;
    private final int startEnergy;
    private final int moveEnergy;
    private final int plantEnergy;
    private final int height;
    private final int width;
    private final double jungleRatio;
    private int multi;
    private Animal selectedAnimal = null;
    private int countDay = 1;
    private boolean showMostCommonGenotype = false;

    public Simulation(int numOfAnimals, int startEnergy, int moveEnergy, int plantEnergy, int height, int width, double jungleRatio){
        this.numOfAnimals = numOfAnimals;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.height = height;
        this.width = width;
        this.jungleRatio = jungleRatio;
    }

    public int getStartEnergy(){
        return this.startEnergy;
    }

    public double getJungleRatio(){
        return this.jungleRatio;
    }

    public int getMulti(){
        return this.multi;
    }

    public Vector2d getLowerLeftJungle(int width, int height, double jungleRatio){
        int jungleWidth = (int) (width*jungleRatio);
        int jungleHeight = (int) (height*jungleRatio);
        return new Vector2d((int)(width- jungleWidth)/2, (int)(height - jungleHeight)/2 );
    }

    public int getJungleWidth(int width, int height, double jungleRatio){
        return (int) (width*jungleRatio);
    }

    public int getJungleHeight(int width, int height, double jungleRatio){
        return (int) (height*jungleRatio);
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

    public boolean getShowMostCommonGenotype(){
        return this.showMostCommonGenotype;
    }

    public void start(){
        if (height > width){
            this.multi = 650/height;
        } else {
            this.multi = 650/width;
        }

        JFrame sim = new JFrame();
        sim.setLayout(new GridBagLayout());

        int jungleWidth = (int) (width*jungleRatio);
        int jungleHeight = (int) (height*jungleRatio);
        Vector2d lowerLeftJungle = new Vector2d((int)(width- jungleWidth)/2, (int)(height - jungleHeight)/2 );
        Vector2d upperRightJungle = lowerLeftJungle.add(new Vector2d(jungleWidth, jungleHeight));
        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight = new Vector2d(width-1, height-1);

        WorldMap map = new WorldMap(lowerLeft, upperRight, lowerLeftJungle, upperRightJungle);
        Statistics stat = new Statistics(map);
        DailyActivity dailyActivity = new DailyActivity(map);

        map.setStartEnergy(startEnergy);
        map.setDayCost(moveEnergy);
        map.setGrassProfit(plantEnergy);

        for(int i = 0; i< numOfAnimals; i++) {
            map.addAnimalOnRandomPlace(width  , height , 0);
        }

        JPanel panel = new MapPaint(this, map);
        panel.setPreferredSize(new Dimension(width*multi, height*multi));

        JPanel buts = new JPanel();
        buts.setLayout(new BoxLayout(buts, BoxLayout.PAGE_AXIS));
        JButton butStart = new JButton("Start");
        JButton butStop = new JButton("Stop");
        JButton butExport = new JButton("Export statistics");
        JButton butGeno = new JButton("(Show / Hide) animals with most common genotype");
        buts.add(butStart);
        buts.add(butStop);
        buts.add(butExport);
        buts.add(butGeno);

        JPanel globalStats = new JPanel();
        JLabel labelDays = new JLabel();
        JLabel labelAlive = new JLabel();
        JLabel labelGrass = new JLabel();
        JLabel labelEnergy = new JLabel();
        JLabel labelLifeSpan = new JLabel();
        JLabel labelChildren = new JLabel();
        JLabel labelGenotype = new JLabel();
        globalStats.add(labelDays);
        globalStats.add(labelAlive);
        globalStats.add(labelGrass);
        globalStats.add(labelEnergy);
        globalStats.add(labelLifeSpan);
        globalStats.add(labelChildren);
        globalStats.add(labelGenotype);
        globalStats.setLayout(new BoxLayout(globalStats, BoxLayout.PAGE_AXIS));


        JPanel animalStats = new JPanel();
        JLabel animalText = new JLabel("Tracked animal: ");
        JLabel animalPosition = new JLabel("Position X: Y:");
        JLabel animalGenotype = new JLabel("Genotype: ");
        JLabel animalChildren = new JLabel("Children: ");
        JLabel animalDescendent = new JLabel("Ancestors: ");
        animalStats.setLayout(new BoxLayout(animalStats, BoxLayout.PAGE_AXIS));
        animalStats.add(animalText);
        animalStats.add(animalPosition);
        animalStats.add(animalGenotype);
        animalStats.add(animalChildren);
        animalStats.add(animalDescendent);

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        sim.add(panel, gc);
        gc.gridx = 1;
        gc.gridy = 0;
        sim.add(buts, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        globalStats.setPreferredSize(new Dimension(550, 120));
        sim.add(globalStats, gc);
        gc.gridx = 1;
        gc.gridy = 1;
        animalStats.setPreferredSize(new Dimension(500, 80));
        sim.add(animalStats, gc);
        sim.pack();
        sim.setVisible(true);

        Timer timer = new Timer(100, new ActionListener() {
            public double deadAnimals = 0;
            public double lifeSpan = 0;
            public double averageLifeSpan = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                for(Animal animal : map.getAnimalsList()){
                    animal.move();
                }

                dailyActivity.copulation(countDay);
                dailyActivity.consumption();
                dailyActivity.decreaseDayEnergy();
                lifeSpan += stat.getDeadAnimalsDays(countDay);
                deadAnimals += stat.getNumberOfDeadAnimals(countDay);

                if(deadAnimals != 0) {
                    averageLifeSpan = lifeSpan / deadAnimals;
                } else averageLifeSpan = 0;

                dailyActivity.removeDeadAnimals(countDay);
                dailyActivity.dailyNewGrass();

                labelDays.setText("Day " + Integer.toString(countDay));
                labelAlive.setText("Current number of animals: " + Integer.toString(map.getAnimalsList().size()));
                labelGrass.setText("Current number of grass: " + Integer.toString(map.getGrassList().size()));
                labelEnergy.setText("Average energy: " + Double.toString(stat.getAnimalsEnergy()));
                labelLifeSpan.setText("Average lifespan: " + Double.toString(averageLifeSpan));
                labelChildren.setText("Average number of children: " + Double.toString(stat.getAverageChildrenNumber()));
                if(map.getAnimalsList().size() > 0) {
                    labelGenotype.setText("Dominant genotype: " + map.getMostCommonGenotype().toString());
                }
                else{
                    labelGenotype.setText("");
                }

                if (selectedAnimal != null){
                    if (selectedAnimal.getEnergy() == 0){
                        animalPosition.setText("Death day: " + (selectedAnimal.getBirthDay() + selectedAnimal.getLengthOfLife() - 1));
                    } else {
                        Vector2d aPosition = selectedAnimal.getPosition();
                        animalPosition.setText("Position X: " + aPosition.getX() + " Y: " + aPosition.getY());
                    }
                    animalGenotype.setText("Genotype: " + selectedAnimal.getGenes().getListGenes().toString());
                    animalChildren.setText("Children: " + selectedAnimal.getChildrenCount());
                    LinkedList<Animal> toCheckDuplicate = new LinkedList<>();
                    animalDescendent.setText("Descendants: " + selectedAnimal.getFamily(toCheckDuplicate));
                }
                panel.repaint();
                sim.pack();
                stat.updateStatistics(countDay, averageLifeSpan);
                countDay++;
            }
        });
        timer.setInitialDelay(100);

        butStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });

        butStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();

            }
        });

        butExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stat.outputStatistics(countDay);
                } catch (IOException ioException) {
                    System.out.println("File write error");
                }
            }
        });

        butGeno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMostCommonGenotype = !showMostCommonGenotype;
                panel.repaint();
            }
        });

        panel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                PointerInfo a = MouseInfo.getPointerInfo();
                Point point = new Point(a.getLocation());
                SwingUtilities.convertPointFromScreen(point, e.getComponent());

                int x=(int) point.getX();
                int y=(int) point.getY();
                Vector2d position = new Vector2d((int)(x/multi), (int)(y/multi));

                if (map.getAnimalByPosition(position) != null){
                    selectedAnimal = map.getAnimalByPosition(position);
                    if (selectedAnimal.getEnergy() == 0){
                        animalPosition.setText("Death day: " + (selectedAnimal.getBirthDay() + selectedAnimal.getLengthOfLife() - 1));
                    } else {
                        Vector2d aPosition = selectedAnimal.getPosition();
                        animalPosition.setText("Position X: " + aPosition.getX() + " Y: " + aPosition.getY());
                    }
                    animalGenotype.setText("Genotype: " + selectedAnimal.getGenes().getListGenes().toString());
                    animalChildren.setText("Children: " + selectedAnimal.getChildrenCount());
                    LinkedList<Animal> toCheckDuplicate = new LinkedList<>();
                    animalDescendent.setText("Descendants: " + selectedAnimal.getFamily(toCheckDuplicate));
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
}
