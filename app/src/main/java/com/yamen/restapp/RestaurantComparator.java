package com.yamen.restapp;

import java.util.Comparator;

public class RestaurantComparator implements Comparator<Restaurant> {

    // TODO: Add new class for comparison
    public int compare(Restaurant left, Restaurant right) {
        return left.getCategory().compareTo(right.getCategory());
    }
}
