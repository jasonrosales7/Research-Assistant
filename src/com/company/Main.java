package com.company;

import java.lang.Math;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            FileWriter myWriter = new FileWriter("filename.csv");

            System.out.println("You have a coin toss, set the odds ");

            //Starting balance
            double bal = 50;
            double startingBal = bal;

            //Player number and attempt per each one
            int numberOfPlayers =  255;
            int numberOfAttempts = 255;

            //Winning probabilities
            double p = 0.65;
            double q = 1 - p;

            //Ratio of win to lose
            double w = 4;
            double l = 6;
            double b = w/l;

            System.out.println();

            double x = Math.random();
            int playerNum = 1;

            //Runs player simulation using kelly theory
            gameTrials(myWriter, bal, startingBal, numberOfPlayers, numberOfAttempts, p, q, b, x, playerNum);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private static void gameTrials(FileWriter myWriter, double bal, double startingBal, int numberOfPlayers, int numberOfAttempts, double p, double q, double b, double x, int playerNum) throws IOException {
        for(int i = 0; i < numberOfPlayers; i++){
            System.out.println("Player " + playerNum);
            double roundedBal = (double)Math.round(bal *100)/100;
            System.out.println("Your starting bal is " + roundedBal);
            myWriter.write(roundedBal + " ");

            for(int j = 0; j < numberOfAttempts; j++){
                //Calculating kelly value
                double roundedKellyBetAmount = getRoundedKellyBetAmount(bal, p, q, b);
                System.out.println("The kelly amount you should bet for this turn is: " + roundedKellyBetAmount);
                if(x > p){
                    bal = kellyWinLogic(myWriter, bal, roundedBal, roundedKellyBetAmount);
                    roundedBal = (double)Math.round(bal *100)/100;
                    System.out.println("Your new bal is " + roundedBal);
                    if(bal <= 0.10){
                        System.out.println("You have too low funds");
                        break;
                    }
                }else{
                    bal = kellyLossLogic(myWriter, bal, roundedBal, roundedKellyBetAmount);
                    roundedBal = (double)Math.round(bal *100)/100;
                    System.out.println("Your new bal is " + roundedBal);
                }
                x = Math.random();
            }
            myWriter.append("\n");
            System.out.println("Ending bal: " + roundedBal);
            playerNum++;
            bal = startingBal;
            System.out.println();
        }
    }

    private static double kellyWinLogic(FileWriter myWriter, double bal, double roundedBal, double roundedKellyBetAmount) throws IOException {
        System.out.println("You lost this bet");
        bal = bal - roundedKellyBetAmount;
        myWriter.write(roundedBal + " ");
        return bal;
    }

    private static double kellyLossLogic(FileWriter myWriter, double bal, double roundedBal, double roundedKellyBetAmount) throws IOException {
        System.out.println("You won this bet");
        bal = bal + roundedKellyBetAmount;
        myWriter.write(roundedBal + " ");
        return bal;
    }

    private static double getRoundedKellyBetAmount(double bal, double p, double q, double b) {
        double kelly = (((b * p) - q)/ b);
        double kellyBetAmount = bal * kelly;
        double roundedKellyBetAmount = (double)Math.round(kellyBetAmount*100)/100;
        return roundedKellyBetAmount;
    }

}
