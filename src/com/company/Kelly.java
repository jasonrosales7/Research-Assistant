package com.company;
import java.lang.Math;
import java.io.FileWriter;
import java.io.IOException;

public class Kelly {
    //Setting starting balance
    double bal = 50;
    double startingBal = bal;

    //Player number and attempt per each one
    int numberOfPlayers =  15;
    int numberOfAttempts = 50;

    //Winning probabilities
    double p = 0.65;
    double q = 1 - p;

    //Ratio of win to lose
    double w = 4;
    double l = 6;
    double b = w/l;
    FileWriter myWriter;

    public Kelly() throws IOException {
        myWriter = new FileWriter("filename.csv");
    }

    private void gameTrialRuns(int playerNum) throws IOException {
        for(int i = 0; i < numberOfPlayers; i++){
            System.out.println("Player " + playerNum);
            System.out.println("Your starting bal is " + bal);
            myWriter.write(bal + ",");
            for(int j = 0; j < numberOfAttempts; j++){
                double kellyBetAmount = getKellyBetAmount();
                double roundedKelly = ((double) Math.round(kellyBetAmount * 100) / 100);
                System.out.println("New betting amount based on balance: " + roundedKelly);
                double x = Math.random();
                if(x >= p){
                    bal = getKellyLossCase(myWriter,
                            bal, kellyBetAmount);
                    if(bal <= 0){
                        System.out.println("You lost");
                        break;
                    }
                }else{
                    bal = getKellyWinCase(myWriter,
                            bal, kellyBetAmount);
                }
            }
            myWriter.append("\n");
            System.out.println("Ending bal: " + bal);
            playerNum++;
            bal = startingBal;
            System.out.println();
        }
    }

    private double getKellyWinCase(FileWriter myWriter, double bal, double kellyBetAmount) throws IOException {
        System.out.println("You won this bet");
        bal = bal + kellyBetAmount;
        bal = ((double) Math.round(bal * 100) / 100);
        myWriter.write(bal + ",");
        System.out.println("Your new bal is " + bal);
        return bal;
    }

    private double getKellyLossCase(FileWriter myWriter, double bal, double kellyBetAmount) throws IOException {
        System.out.println("You lost this bet");
        bal = bal - kellyBetAmount;
        bal = ((double) Math.round(bal * 100) / 100);
        System.out.println("Your new bal is " + bal);
        myWriter.write(bal + ",");
        return bal;
    }

    public void run() {

        try {
            //Calculating kelly value

            int playerNum = 1;

            //Runs player simulation using kelly theory
            gameTrialRuns(playerNum);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private double getKellyBetAmount() {
        double f = (((b * p) - q) / b);
        return bal * f;
    }

    public static void main(String[] args) {
        try {
            Kelly k = new Kelly();
            k.run();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}