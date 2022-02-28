package agh.cs.projektPO.Visualisation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.FileReader;

public class World {
    public static void main(String[] args) throws Exception {
        JFrame settings = new JFrame();
        settings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Object obj = new JSONParser().parse(new FileReader("parameters.json"));
        JSONObject jo = (JSONObject) obj;
        String numberOfAnimals = jo.get("numberOfAnimals").toString();
        String startEnergy = jo.get("startEnergy").toString();
        String dayCostEnergy = jo.get("dayCostEnergy").toString();
        String plantProfit = jo.get("plantProfit").toString();
        String height = jo.get("height").toString();
        String width = jo.get("width").toString();
        String jungleRatio = jo.get("jungleRatio").toString();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        JLabel lab1 = new JLabel("Initial settings: ");
        lab1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lab1);

        JPanel panelAnimal = new JPanel();
        JLabel labelAnimal = new JLabel("Initial number of animals: ");
        JTextField textAnimal = new JTextField(numberOfAnimals,10);
        textAnimal.setMaximumSize(textAnimal.getPreferredSize());
        panelAnimal.add(labelAnimal);
        panelAnimal.add(textAnimal);
        panel.add(panelAnimal);

        JPanel panelEnergy = new JPanel();
        JLabel labelEnergy = new JLabel("Initial energy of animals: ");
        JTextField textEnergy = new JTextField(startEnergy,10);
        textEnergy.setMaximumSize(textEnergy.getPreferredSize());
        panelEnergy.add(labelEnergy);
        panelEnergy.add(textEnergy);
        panel.add(panelEnergy);

        JPanel panelMoveEnergy = new JPanel();
        JLabel labelMoveEnergy = new JLabel("Daily energy: ");
        JTextField textMoveEnergy = new JTextField(dayCostEnergy,10);
        textMoveEnergy.setMaximumSize(textMoveEnergy.getPreferredSize());
        panelMoveEnergy.add(labelMoveEnergy);
        panelMoveEnergy.add(textMoveEnergy);
        panel.add(panelMoveEnergy);

        JPanel panelPlantEnergy = new JPanel();
        JLabel labelPlantEnergy = new JLabel("Energy from grass: ");
        JTextField textPlantEnergy = new JTextField(plantProfit,10);
        textPlantEnergy.setMaximumSize(textPlantEnergy.getPreferredSize());
        panelPlantEnergy.add(labelPlantEnergy);
        panelPlantEnergy.add(textPlantEnergy);
        panel.add(panelPlantEnergy);

        JPanel panelMapHeight = new JPanel();
        JLabel labelMapHeight = new JLabel("World height: ");
        JTextField textMapHeight = new JTextField(height,10);
        textMapHeight.setMaximumSize(textMapHeight.getPreferredSize());
        panelMapHeight.add(labelMapHeight);
        panelMapHeight.add(textMapHeight);
        panel.add(panelMapHeight);

        JPanel panelMapWidth = new JPanel();
        JLabel labelMapWidth = new JLabel("World width: ");
        JTextField textMapWidth = new JTextField(width,10);
        textMapWidth.setMaximumSize(textMapWidth.getPreferredSize());
        panelMapWidth.add(labelMapWidth);
        panelMapWidth.add(textMapWidth);
        panel.add(panelMapWidth);

        JPanel panelJungleRatio = new JPanel();
        JLabel labelJungleRatio = new JLabel("Jungle ratio: ");
        JTextField textJungleRatio = new JTextField(jungleRatio,10);
        textJungleRatio.setMaximumSize(textJungleRatio.getPreferredSize());
        panelJungleRatio.add(labelJungleRatio);
        panelJungleRatio.add(textJungleRatio);
        panel.add(panelJungleRatio);

        JButton but = new JButton("Start");
        but.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(but);

        settings.add(panel);
        settings.setSize(400,500);
        settings.setVisible(true);

        but.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Simulation sim = new Simulation(Integer.parseInt(textAnimal.getText()),
                        Integer.parseInt(textEnergy.getText()),
                        Integer.parseInt(textMoveEnergy.getText()),
                        Integer.parseInt(textPlantEnergy.getText()),
                        Integer.parseInt(textMapHeight.getText()),
                        Integer.parseInt(textMapWidth.getText()),
                        Double.parseDouble(textJungleRatio.getText())
                );
                sim.start();
            }
        });
    }
}
