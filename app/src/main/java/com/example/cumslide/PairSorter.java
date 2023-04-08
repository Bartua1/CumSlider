package com.example.cumslide;

import com.example.cumslide.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PairSorter {

    public static Pair getClosestPair(List<Pair> pairs, Pair reference) {
        // Sort the list by distance to the reference point
        Collections.sort(pairs, new Comparator<Pair>() {
            @Override
            public int compare(Pair p1, Pair p2) {
                double dist1 = p1.getDistance(reference);
                double dist2 = p2.getDistance(reference);
                return Double.compare(dist1, dist2);
            }
        });

        // Return the closest pair (i.e., the first pair in the sorted list)
        return pairs.get(0);
    }

}
