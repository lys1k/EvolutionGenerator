package agh.cs.projektPO.Classes;
import java.util.Objects;

public class Vector2d {
    final int x;
    final int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String toString(){
        return "(" + x + "," + y + ")";
    }

    public boolean precedes(Vector2d other){
        return (this.x <= other.x && this.y <= other.y);
    }

    public boolean follows(Vector2d other){
        return (this.x >= other.x && this.y >= other.y);
    }

    public Vector2d upperRight(Vector2d other){
        int x;
        int y;

        x = Math.max(this.x, other.x);
        y = Math.max(this.y, other.y);
        return new Vector2d(x,y);
    }

    public Vector2d lowerLeft(Vector2d other){
        int x;
        int y;
        x = Math.min(this.x, other.x);
        y = Math.min(this.y, other.y);
        return new Vector2d(x,y);
    }

    public Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x, this.y + other.y );
    }

    public Vector2d subtract(Vector2d other){
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    public boolean equals(Object other){
        if(this == other)
            return true;
        if(!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return(this.x == that.x && this.y == that.y);
    }

    public Vector2d opposite(){
        return new Vector2d(-this.x, -this.y);
    }

    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
