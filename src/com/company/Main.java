package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<String> rivalries = new ArrayList<>();
    public static ArrayList<Team> teams = new ArrayList<>();
    public static ArrayList<Team> r64, r32, s16, e8, f4, c = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        File file = new File("Rivalries");

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
        r32 = seasonReader("1985");
        for (Team a : r32) {
            System.out.println(a.schoolName);
        }
    }
    public static Team teamFinder(String teamName) {
        for (Team searched : teams) {
            if (searched.schoolName.equalsIgnoreCase(teamName)) {
                return searched;
            }
        }
        System.out.println("error: team not found");
        return teams.get(0);
    }
    public static Team teamFinder(int ID) {
        String id = Integer.toString(ID);
        for (Team searched : teams) {
            if (searched.id.equalsIgnoreCase(id)) {
                return searched;
            }
        }
        System.out.println("error: team not found");
        return teams.get(0);
    }
    public static ArrayList<Team> seasonReader(String year) throws IOException {
        File file = new File("DataFiles/NCAATourneyCompactResults.csv");
        Scanner fileReader = new Scanner(file);
        ArrayList<Team> sTeams = new ArrayList<>();
        int startDay = 0;
        if (Integer.parseInt(year) >= 2001) {
            startDay = 134;
        }
        else {
            startDay = 136;
        }
        while(fileReader.hasNextLine() ) {
            String [] temp = fileReader.nextLine().split(",");
            if (temp[0].equalsIgnoreCase(year) && Integer.parseInt(temp[1]) == startDay) {
                sTeams.add(teamFinder(Integer.parseInt(temp[2])));
                sTeams.add(teamFinder(Integer.parseInt(temp[4])));
            }
        }
        return sTeams;
    }
}