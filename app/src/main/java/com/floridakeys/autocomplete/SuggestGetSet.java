package com.floridakeys.autocomplete;

/**
 * @description Data Structure for autocomplete
 */

public class SuggestGetSet {
    String name;

    public SuggestGetSet(String name){
        this.setName(name);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
