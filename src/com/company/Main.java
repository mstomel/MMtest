package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<String> rivalries = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        File file = new File("Rivalries");
        ArrayList<Team> teams = new ArrayList<>();

        Scanner fileReader = new Scanner(file);
        while(fileReader.hasNextLine()) {
        rivalries.add(fileReader.nextLine());
        }

        file = new File("DataFiles/TeamSpellings.csv");
        fileReader = new Scanner(file);
        while(fileReader.hasNextLine()) {
            String [] nameID = fileReader.nextLine().split(",");
            Team temp = new Team(nameID[0], nameID[1]);
            teams.add(temp);
        }

        System.out.println(teams);
    }
}