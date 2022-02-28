package agh.cs.projektPO.Interfaces;
import agh.cs.projektPO.Classes.Animal;
import agh.cs.projektPO.Classes.Vector2d;

public interface IPositionChangeObserver {
    void onPositionChange(Animal animal, Vector2d oldPosition);
}
