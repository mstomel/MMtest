package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static ArrayList<String> rivalries = new ArrayList<>();
    private static ArrayList<Team> teams = new ArrayList<>();
    private static ArrayList<Team> r64 = new ArrayList<>();
    private static ArrayList<Team> r32 = new ArrayList<>();
    private static ArrayList<Team> s16 = new ArrayList<>();
    private static ArrayList<Team> e8 = new ArrayList<>();
    private static ArrayList<Team> f4 = new ArrayList<>();
    private static ArrayList<Team> c = new ArrayList<>();
    //private static ArrayList<Team> elos = new ArrayList<>();
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
        /*file = new File("season_elos.csv");
        fileReader = new Scanner(file);
        while(fileReader.hasNextLine()) {
            String [] temp = fileReader.nextLine().split(",");
            if (temp[1].equalsIgnoreCase("season")) {
                continue;
            }
            else{
                teamFinder(temp[3]).elo.put(temp[1],Double.valueOf(temp[2]));
            }
        }*/
        System.out.println("load new tournament(1), Test previous tournament(2), Test previous with rivalTrack(3), or AlgoTest(4)");
        Scanner mode = new Scanner(System.in);
        int mo = mode.nextInt();
        if (mo == 1) {
            newRunner();
        }
        else if (mo == 2) {
            oldRunner();
        }
        else if (mo == 3) {
            oldRunnerR();
        }
        else if (mo == 4) {
            AlgoTest();
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
    private static Team teamFinder(int ID) {
        String id = Integer.toString(ID);
        for (Team searched : teams) {
            if (searched.id.equalsIgnoreCase(id)) {
                return searched;
            }
        }
        System.out.println("error: team not found");
        return teams.get(0);
    }
    private static ArrayList<Team> seasonReader(String year) throws IOException {
        File file = new File("DataFiles/NCAATourneyCompactResults.csv");
        Scanner fileReader = new Scanner(file);
        ArrayList<Team> sTeams = new ArrayList<>();
        int startDay = 136;
        /*if (Integer.parseInt(year) >= 2001) {
            startDay = 134;
        }
        else {
            startDay = 136;
        }*/
        while(fileReader.hasNextLine() ) {
            String [] temp = fileReader.nextLine().split(",");
            if (Integer.parseInt(year) >= 2011) {
                if (temp[0].equalsIgnoreCase(year) && Integer.parseInt(temp[1]) == startDay) {
                    sTeams.add(teamFinder(Integer.parseInt(temp[2])));
                    sTeams.add(teamFinder(Integer.parseInt(temp[4])));
                }
                else if (temp[0].equalsIgnoreCase(year) && Integer.parseInt(temp[1]) == startDay+1) {
                    sTeams.add(teamFinder(Integer.parseInt(temp[2])));
                    sTeams.add(teamFinder(Integer.parseInt(temp[4])));
                }
                else if (temp[0].equalsIgnoreCase(year) && Integer.parseInt(temp[1]) == startDay+2) {
                    sTeams.add(teamFinder(Integer.parseInt(temp[2])));
                    sTeams.add(teamFinder(Integer.parseInt(temp[4])));
                }
            }
            else if (Integer.parseInt(year) >= 1985) {
                if (temp[0].equalsIgnoreCase(year) && Integer.parseInt(temp[1]) == startDay) {
                    sTeams.add(teamFinder(Integer.parseInt(temp[2])));
                    sTeams.add(teamFinder(Integer.parseInt(temp[4])));
                }
                else if (temp[0].equalsIgnoreCase(year) && Integer.parseInt(temp[1]) == startDay+1) {
                    sTeams.add(teamFinder(Integer.parseInt(temp[2])));
                    sTeams.add(teamFinder(Integer.parseInt(temp[4])));
                }
            }
            else {
                if (temp[0].equalsIgnoreCase(year) && Integer.parseInt(temp[1]) == startDay) {
                    sTeams.add(teamFinder(Integer.parseInt(temp[2])));
                    sTeams.add(teamFinder(Integer.parseInt(temp[4])));
                }
            }
        }
        return sTeams;
    }
    private static void recordBuilder(String year) throws IOException {
        File file = new File("PrelimData2018/RegularSeasonCompactResults_Prelim2018.csv");
        Scanner fileReader = new Scanner(file);
        while (fileReader.hasNextLine()) {
            String [] temp = fileReader.nextLine().split(",");
            if (temp[0].equalsIgnoreCase("season")){
                continue;
            }
            if (temp[0].equalsIgnoreCase(year)) {
                teamFinder(Integer.parseInt(temp[2])).wins += 1;
                teamFinder(Integer.parseInt(temp[4])).losses += 1;
            }
        }
    }
    private static void oldRunner() throws IOException {
        Scanner mode = new Scanner(System.in);
        System.out.println("enter year");
        String year = mode.next();
        r64 = seasonReader(year);
        recordBuilder(year);

        //round of 64
        System.out.println("round of 64 matchups:");
        for (int i = 0; i < 64; i++) {
            if (i%2==0) {
                System.out.print(r64.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r64.get(i).schoolName);
            }
        }
        for (int i = 0; i < 64; i+=2){
            if ((r64.get(i).wins-r64.get(i).losses)>(r64.get(i+1).wins-r64.get(i+1).losses)) {
                r32.add(r64.get(i));
            }
            else {
                r32.add(r64.get(i+1));
            }
        }

        //round of 32 predictor
        System.out.println();
        System.out.println("round of 32 matchups:");
        for (int i = 0; i < 32; i++) {
            if (i%2==0) {
                System.out.print(r32.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r32.get(i).schoolName);
            }
        }
        for (int i = 0; i < 32; i+=2){
            if ((r32.get(i).wins-r32.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                s16.add(r32.get(i));
            }
            else {
                s16.add(r32.get(i+1));
            }
        }

        //round of 16 predictor
        System.out.println();
        System.out.println("sweet 16 matchups:");
        for (int i = 0; i < 16; i++) {
            if (i%2==0) {
                System.out.print(s16.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(s16.get(i).schoolName);
            }
        }
        for (int i = 0; i < 16; i+=2){
            if ((s16.get(i).wins-s16.get(i).losses)>(s16.get(i+1).wins-s16.get(i+1).losses)) {
                e8.add(s16.get(i));
            }
            else {
                e8.add(s16.get(i+1));
            }
        }

        //elite 8 predictor
        System.out.println();
        System.out.println("elite eight matchups:");
        for (int i = 0; i < 8; i++) {
            if (i%2==0) {
                System.out.print(e8.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(e8.get(i).schoolName);
            }
        }
        for (int i = 0; i < 8; i+=2){
            if ((e8.get(i).wins-e8.get(i).losses)>(e8.get(i+1).wins-e8.get(i+1).losses)) {
                f4.add(e8.get(i));
            }
            else {
                f4.add(e8.get(i+1));
            }
        }

        //final 4 predictor
        System.out.println();
        System.out.println("final four matchups:");
        for (int i = 0; i < 4; i++) {
            if (i%2==0) {
                System.out.print(f4.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(f4.get(i).schoolName);
            }
        }
        for (int i = 0; i < 4; i+=2){
            if ((f4.get(i).wins-f4.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                c.add(f4.get(i));
            }
            else {
                c.add(f4.get(i+1));
            }
        }

        //champ predictor
        System.out.println();
        System.out.println("championship game:");
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                System.out.print(c.get(i).schoolName + " vs ");
            } else {
                System.out.println(c.get(i).schoolName);
            }
        }
        for (int i = 0; i < 2; i+=2){
            if ((c.get(i).wins-c.get(i).losses)>(c.get(i+1).wins-c.get(i+1).losses)) {
                System.out.println("champion is: " + c.get(i).schoolName);
            }
            else {
                System.out.println("champion is: " + c.get(i+1).schoolName);
            }
        }
    }
    private static void oldRunnerR() throws IOException {
        Scanner mode = new Scanner(System.in);
        System.out.println("enter year");
        String year = mode.next();
        r64 = seasonReader(year);
        recordBuilder(year);

        //round of 64
        System.out.println("round of 64 matchups:");
        for (int i = 0; i < 64; i++) {
            if (i%2==0) {
                System.out.print(r64.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r64.get(i).schoolName);
            }
        }
        for (int i = 0; i < 64; i+=2){
            if (rivalries.contains(r64.get(i).schoolName + " " + r64.get(i+1).schoolName) || rivalries.contains(r64.get(i+1).schoolName + " " + r64.get(i).schoolName)) {
                double temp = Math.random()*1;
                if (temp >= .5) {
                    r32.add(r64.get(i));
                }
                else {
                    r32.add(r64.get(i+1));
                }
            }
            else {
                if ((r64.get(i).wins-r64.get(i).losses)>(r64.get(i+1).wins-r64.get(i+1).losses)) {
                    r32.add(r64.get(i));
                }
                else {
                    r32.add(r64.get(i+1));
                }
            }
        }

        //round of 32 predictor
        System.out.println();
        System.out.println("round of 32 matchups:");
        for (int i = 0; i < 32; i++) {
            if (i%2==0) {
                System.out.print(r32.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r32.get(i).schoolName);
            }
        }
        for (int i = 0; i < 32; i+=2){
            if (rivalries.contains(r32.get(i).schoolName + " " + r32.get(i+1).schoolName) || rivalries.contains(r32.get(i+1).schoolName + " " + r32.get(i).schoolName)) {
                double temp = Math.random()*1;
                if (temp >= .5) {
                    s16.add(r32.get(i));
                }
                else {
                    s16.add(r32.get(i+1));
                }
            }
            else {
                if ((r32.get(i).wins-r32.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                    s16.add(r32.get(i));
                }
                else {
                    s16.add(r32.get(i+1));
                }
            }
        }

        //round of 16 predictor
        System.out.println();
        System.out.println("sweet 16 matchups:");
        for (int i = 0; i < 16; i++) {
            if (i%2==0) {
                System.out.print(s16.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(s16.get(i).schoolName);
            }
        }
        for (int i = 0; i < 16; i+=2){
            if (rivalries.contains(s16.get(i).schoolName + " " + s16.get(i+1).schoolName) || rivalries.contains(s16.get(i+1).schoolName + " " + s16.get(i).schoolName)) {
                double temp = Math.random()*1;
                if (temp >= .5) {
                    e8.add(s16.get(i));
                }
                else {
                    e8.add(s16.get(i+1));
                }
            }
            else {
                if ((s16.get(i).wins-s16.get(i).losses)>(s16.get(i+1).wins-s16.get(i+1).losses)) {
                    e8.add(s16.get(i));
                }
                else {
                    e8.add(s16.get(i+1));
                }
            }
        }

        //elite 8 predictor
        System.out.println();
        System.out.println("elite eight matchups:");
        for (int i = 0; i < 8; i++) {
            if (i%2==0) {
                System.out.print(e8.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(e8.get(i).schoolName);
            }
        }
        for (int i = 0; i < 8; i+=2){
            if (rivalries.contains(e8.get(i).schoolName + " " + e8.get(i+1).schoolName) || rivalries.contains(e8.get(i+1).schoolName + " " + e8.get(i).schoolName)) {
                double temp = Math.random()*1;
                if (temp >= .5) {
                    f4.add(e8.get(i));
                }
                else {
                    f4.add(e8.get(i+1));
                }
            }
            else {
                if ((e8.get(i).wins-e8.get(i).losses)>(e8.get(i+1).wins-e8.get(i+1).losses)) {
                    f4.add(e8.get(i));
                }
                else {
                    f4.add(e8.get(i+1));
                }
            }
        }

        //final 4 predictor
        System.out.println();
        System.out.println("final four matchups:");
        for (int i = 0; i < 4; i++) {
            if (i%2==0) {
                System.out.print(f4.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(f4.get(i).schoolName);
            }
        }
        for (int i = 0; i < 4; i+=2){
            if (rivalries.contains(f4.get(i).schoolName + " " + f4.get(i+1).schoolName) || rivalries.contains(f4.get(i+1).schoolName + " " + f4.get(i).schoolName)) {
                double temp = Math.random()*1;
                if (temp >= .5) {
                    c.add(f4.get(i));
                }
                else {
                    c.add(f4.get(i+1));
                }
            }
            else {
                if ((f4.get(i).wins-f4.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                    c.add(f4.get(i));
                }
                else {
                    c.add(f4.get(i+1));
                }
            }
        }

        //champ predictor
        System.out.println();
        System.out.println("championship game:");
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                System.out.print(c.get(i).schoolName + " vs ");
            } else {
                System.out.println(c.get(i).schoolName);
            }
        }
        for (int i = 0; i < 2; i+=2){
            if (rivalries.contains(c.get(i).schoolName + " " + c.get(i+1).schoolName) || rivalries.contains(c.get(i+1).schoolName + " " + c.get(i).schoolName)) {
                double temp = Math.random()*1;
                if (temp >= .5) {
                    System.out.println("champion is: " + c.get(i).schoolName);
                }
                else {
                    System.out.println("champion is: " + c.get(i+1).schoolName);
                }
            }
            else {
                if ((c.get(i).wins-c.get(i).losses)>(c.get(i+1).wins-c.get(i+1).losses)) {
                    System.out.println("champion is: " + c.get(i).schoolName);
                }
                else {
                    System.out.println("champion is: " + c.get(i+1).schoolName);
                }
            }
        }
    }
    private static void AlgoTest() throws IOException {
        Scanner mode = new Scanner(System.in);
        System.out.println("enter year");
        String year = mode.next();
        r64 = seasonReader(year);
        recordBuilder(year);

        //round of 64
        System.out.println("round of 64 matchups:");
        for (int i = 0; i < 64; i++) {
            if (i%2==0) {
                System.out.print(r64.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r64.get(i).schoolName);
            }
        }
        for (int i = 0; i < 64; i+=2){
            if ((r64.get(i).wins-r64.get(i).losses)>(r64.get(i+1).wins-r64.get(i+1).losses)) {
                r32.add(r64.get(i));
            }
            else {
                r32.add(r64.get(i+1));
            }
        }

        //round of 32 predictor
        System.out.println();
        System.out.println("round of 32 matchups:");
        for (int i = 0; i < 32; i++) {
            if (i%2==0) {
                System.out.print(r32.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r32.get(i).schoolName);
            }
        }
        for (int i = 0; i < 32; i+=2){
            if ((r32.get(i).wins-r32.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                s16.add(r32.get(i));
            }
            else {
                s16.add(r32.get(i+1));
            }
        }

        //round of 16 predictor
        System.out.println();
        System.out.println("sweet 16 matchups:");
        for (int i = 0; i < 16; i++) {
            if (i%2==0) {
                System.out.print(s16.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(s16.get(i).schoolName);
            }
        }
        for (int i = 0; i < 16; i+=2){
            if ((s16.get(i).wins-s16.get(i).losses)>(s16.get(i+1).wins-s16.get(i+1).losses)) {
                e8.add(s16.get(i));
            }
            else {
                e8.add(s16.get(i+1));
            }
        }

        //elite 8 predictor
        System.out.println();
        System.out.println("elite eight matchups:");
        for (int i = 0; i < 8; i++) {
            if (i%2==0) {
                System.out.print(e8.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(e8.get(i).schoolName);
            }
        }
        for (int i = 0; i < 8; i+=2){
            if ((e8.get(i).wins-e8.get(i).losses)>(e8.get(i+1).wins-e8.get(i+1).losses)) {
                f4.add(e8.get(i));
            }
            else {
                f4.add(e8.get(i+1));
            }
        }

        //final 4 predictor
        System.out.println();
        System.out.println("final four matchups:");
        for (int i = 0; i < 4; i++) {
            if (i%2==0) {
                System.out.print(f4.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(f4.get(i).schoolName);
            }
        }
        for (int i = 0; i < 4; i+=2){
            if ((f4.get(i).wins-f4.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                c.add(f4.get(i));
            }
            else {
                c.add(f4.get(i+1));
            }
        }

        //champ predictor
        System.out.println();
        System.out.println("championship game:");
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                System.out.print(c.get(i).schoolName + " vs ");
            } else {
                System.out.println(c.get(i).schoolName);
            }
        }
        for (int i = 0; i < 2; i+=2){
            if ((c.get(i).wins-c.get(i).losses)>(c.get(i+1).wins-c.get(i+1).losses)) {
                System.out.println("champion is: " + c.get(i).schoolName);
            }
            else {
                System.out.println("champion is: " + c.get(i+1).schoolName);
            }
        }
    }

    private static ArrayList<Team> seasonReader18() throws IOException {
        File file = new File("2018Matchups.csv");
        Scanner fileReader = new Scanner(file);
        ArrayList<Team> sTeams = new ArrayList<>();
        while(fileReader.hasNextLine() ) {
            String [] temp = fileReader.nextLine().split(",");
            if (temp[0].equalsIgnoreCase("East")||temp[0].equalsIgnoreCase("South")||temp[0].equalsIgnoreCase("West")||temp[0].equalsIgnoreCase("Midwest")) {
                continue;
            }
            else if (temp[2].equalsIgnoreCase("x")) {
                Team Temp = new Team("x","0");
                sTeams.add(Temp);
            }
            else {
                sTeams.add(teamFinder(Integer.parseInt(temp[2])));
            }
        }
        return sTeams;
    }
    private static void newRunner() throws IOException {
        Scanner mode = new Scanner(System.in);
        System.out.println("enter year");
        String year = mode.next();
        r64 = seasonReader18();
        recordBuilder(year);

        //round of 64
        System.out.println("round of 64 matchups:");
        for (int i = 0; i < 64; i++) {
            if (i%2==0) {
                System.out.print(r64.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r64.get(i).schoolName);
            }
        }
        for (int i = 0; i < 64; i+=2){
            if ((r64.get(i).wins-r64.get(i).losses)>(r64.get(i+1).wins-r64.get(i+1).losses)) {
                r32.add(r64.get(i));
            }
            else {
                r32.add(r64.get(i+1));
            }
        }

        //round of 32 predictor
        System.out.println();
        System.out.println("round of 32 matchups:");
        for (int i = 0; i < 32; i++) {
            if (i%2==0) {
                System.out.print(r32.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(r32.get(i).schoolName);
            }
        }
        for (int i = 0; i < 32; i+=2){
            if ((r32.get(i).wins-r32.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                s16.add(r32.get(i));
            }
            else {
                s16.add(r32.get(i+1));
            }
        }

        //round of 16 predictor
        System.out.println();
        System.out.println("sweet 16 matchups:");
        for (int i = 0; i < 16; i++) {
            if (i%2==0) {
                System.out.print(s16.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(s16.get(i).schoolName);
            }
        }
        for (int i = 0; i < 16; i+=2){
            if ((s16.get(i).wins-s16.get(i).losses)>(s16.get(i+1).wins-s16.get(i+1).losses)) {
                e8.add(s16.get(i));
            }
            else {
                e8.add(s16.get(i+1));
            }
        }

        //elite 8 predictor
        System.out.println();
        System.out.println("elite eight matchups:");
        for (int i = 0; i < 8; i++) {
            if (i%2==0) {
                System.out.print(e8.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(e8.get(i).schoolName);
            }
        }
        for (int i = 0; i < 8; i+=2){
            if ((e8.get(i).wins-e8.get(i).losses)>(e8.get(i+1).wins-e8.get(i+1).losses)) {
                f4.add(e8.get(i));
            }
            else {
                f4.add(e8.get(i+1));
            }
        }

        //final 4 predictor
        System.out.println();
        System.out.println("final four matchups:");
        for (int i = 0; i < 4; i++) {
            if (i%2==0) {
                System.out.print(f4.get(i).schoolName + " vs ");
            }
            else {
                System.out.println(f4.get(i).schoolName);
            }
        }
        for (int i = 0; i < 4; i+=2){
            if ((f4.get(i).wins-f4.get(i).losses)>(r32.get(i+1).wins-r32.get(i+1).losses)) {
                c.add(f4.get(i));
            }
            else {
                c.add(f4.get(i+1));
            }
        }

        //champ predictor
        System.out.println();
        System.out.println("championship game:");
        for (int i = 0; i < 2; i++) {
            if (i % 2 == 0) {
                System.out.print(c.get(i).schoolName + " vs ");
            } else {
                System.out.println(c.get(i).schoolName);
            }
        }
        for (int i = 0; i < 2; i+=2){
            if ((c.get(i).wins-c.get(i).losses)>(c.get(i+1).wins-c.get(i+1).losses)) {
                System.out.println("champion is: " + c.get(i).schoolName);
            }
            else {
                System.out.println("champion is: " + c.get(i+1).schoolName);
            }
        }
    }
}