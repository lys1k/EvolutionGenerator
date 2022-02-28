package agh.cs.projektPO.Interfaces;
import agh.cs.projektPO.Classes.Vector2d;

public interface IMapElement {
    String toString();
    Vector2d getPosition();
    void move();
    void addObserver(IPositionChangeObserver observer);
    void removeObserver(IPositionChangeObserver observer);
}
