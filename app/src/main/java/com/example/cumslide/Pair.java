package com.example.cumslide;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Pair {
    private int x;
    private int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Pair(Set<Pair> region) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Pair pair : region) {
            minX = Math.min(minX, pair.getX());
            maxX = Math.max(maxX, pair.getX());
            minY = Math.min(minY, pair.getY());
            maxY = Math.max(maxY, pair.getY());
        }
        this.x = minX;
        this.y = minY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Set<Pair> getRegion() {
        Set<Pair> region = new HashSet<>();
        region.add(this);
        return region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return x == pair.x &&
                y == pair.y;
    }

    public double getDistance(Pair p){
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
