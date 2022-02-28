package agh.cs.projektPO.Enums;

import agh.cs.projektPO.Classes.Vector2d;

public enum MapDirection {
    NORTH, SOUTH, WEST, EAST, NORTHWEST, SOUTHWEST, NORTHEAST, SOUTHEAST;

    public String toString() {
        switch (this) {
            case NORTH:
                return "↑ - północ";
            case SOUTH:
                return "↓ - południe";
            case WEST:
                return "← - zachód ";
            case EAST:
                return "→ - wschód";
            case NORTHWEST:
                return "↖ - północny - zachód";
            case SOUTHWEST:
                return "↙ - południowy - zachód";
            case NORTHEAST:
                return "↗ północny - wschód";
            case SOUTHEAST:
                return "↘ - południowy wschód";
            default:
                return null;
        }
    }

    public MapDirection next() {
        switch (this) {
            case NORTH:
                return NORTHEAST;
            case SOUTH:
                return SOUTHWEST;
            case WEST:
                return NORTHWEST;
            case EAST:
                return SOUTHEAST;
            case NORTHWEST:
                return NORTH;
            case SOUTHWEST:
                return WEST;
            case NORTHEAST:
                return EAST;
            case SOUTHEAST:
                return SOUTH;
            default:
                return null;
        }
    }

    public MapDirection previous() {
        switch (this) {
            case NORTH:
                return NORTHWEST;
            case SOUTH:
                return SOUTHEAST;
            case WEST:
                return SOUTHWEST;
            case EAST:
                return NORTHEAST;
            case NORTHWEST:
                return WEST;
            case SOUTHWEST:
                return SOUTH;
            case NORTHEAST:
                return NORTH;
            case SOUTHEAST:
                return EAST;
        }
        return null;
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case NORTH:
                return new Vector2d(0, 1);
            case SOUTH:
                return new Vector2d(0, -1);
            case WEST:
                return new Vector2d(-1, 0);
            case EAST:
                return new Vector2d(1, 0);
            case NORTHWEST:
                return new Vector2d(-1, 1);
            case SOUTHWEST:
                return new Vector2d(-1, -1);
            case NORTHEAST:
                return new Vector2d(1, 1);
            case SOUTHEAST:
                return new Vector2d(1, -1);
            default:
                return null;
        }
    }

    public MapDirection getRandomDirection(){
        int random = (int) (Math.random() * (7));
        return MapDirection.values()[random];
    }
}



