package agh.cs.projektPO.Interfaces;
import agh.cs.projektPO.Classes.Vector2d;

public interface IWorldMap {
    boolean canMoveTo(Vector2d position);
    boolean isOccupied(Vector2d position);
    Object objectAt(Vector2d position);
}