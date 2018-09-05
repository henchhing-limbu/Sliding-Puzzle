package com.techexchange.mobileapps.assignment1;

import java.util.HashMap;

class NumberImages {
    private Integer number;
    private Integer state;
    private Integer location;
    private HashMap<Integer, Integer> resIdToIndex = new HashMap<>();

    public NumberImages(Integer number, Integer state, Integer location) {
        this.number = number;
        this.state = state;
        this.location = location;
    }

    public Integer getState() {
        return state;
    }

    public Integer getLocation() {
        return location;
    }

    public Integer getNumber() {
        return number;
    }
}
