package com.company;

import java.util.HashMap;
import java.util.Map;

class Team {

    String schoolName, id;
    int wins, losses;
    //Map<String, Double> elo = new HashMap<>();
    Team(String name, String ID){
        schoolName = name;
        id = ID;
    }
}