// Programmer: Gregory Smith
// Class: CS 145
// Date: 04/12/2022
// Assignment: Lab 1 - Guessing Game
// Reference: Chapter 1-9,
// https://docs.oracle.com/javase/8/docs/api/java/lang/String.html,
// https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
//
// Purpose: The purpose of this program is to prompt a user to play a guessing game which consists
// of guessing what number was chosen from a random integer range. Play continues until the user
// wishes to finish, at which point, summary statistics of the games played are displayed. 
//
// For extra credit I had the range of values that the number is chosen from be random as well
// and created classes for stat tracking and random number generation.
//
// No real issues in terms of code implementation.

import java.util.Scanner;
import java.util.Random;
import java.lang.NumberFormatException; // handle String->int conversion attempts

public class GSmithGuessingGame {

   public static void main(String[] args) {
      instructions();
      playGamesMenu();

      return;
      
   } // end of main

   // print out to user game instructions
   public static void instructions() {
      System.out.println("This program allows you to play a guessing game.");
      System.out.println("I will think of a number between two");
      System.out.println("numbers and will allow you to guess until");
      System.out.println("you get it.  For each guess, I will tell you");
      System.out.println("whether the right answer is higher or lower");
      System.out.println("than your guess.");
      return;

   } // end of instructions

   // instantiate summary stats object and call playGames and summary methods
   public static void playGamesMenu() {
      // tracks summary stats and numTries of current game
      GuessingGamesStats guessingGamesStatsObj = new GuessingGamesStats();

      // play games and print summary stats
      playGames(guessingGamesStatsObj);
      summary(guessingGamesStatsObj);
      
   } // end of playGamesMenu

   // instantiate random numbers object, and call playGame until exited by user
   public static void playGames(GuessingGamesStats guessingGamesStatsObj) {
      // create range to sample randomInt from, and randomInt itself 
      GuessingGamesNumbers guessingGamesNumbersObj = new GuessingGamesNumbers();
   
      // declare String for staying in game loop
      String gameContinue;
      
      Scanner inputObj = new Scanner(System.in);

      do {
         playGame(guessingGamesStatsObj, guessingGamesNumbersObj);
         System.out.print("Do you want to play again? (\"Y\" or \"y\" for yes) ");
         gameContinue = inputObj.nextLine();

      } while (gameContinue.equalsIgnoreCase("Y")); // end of do-while
      
      return;

   } // end of playGames

   public static void playGame(GuessingGamesStats guessingGamesStatsObj, 
      GuessingGamesNumbers guessingGamesNumbersObj) {
      
      System.out.printf("\nI'm thinking of a number between %d and %d...%n",
         guessingGamesNumbersObj.getStartRange(), guessingGamesNumbersObj.getEndRange());

      playGameWhile(guessingGamesNumbersObj.getRandomInt(), guessingGamesStatsObj);
      System.out.printf("You got it right in %d guesses.%n", guessingGamesStatsObj.getNumTries());
      
      guessingGamesStatsObj.startNewGame();
      guessingGamesNumbersObj.setNewNumbers();
      return;

   } // end of playGame

   // user guesses random number until correct 
   public static void playGameWhile(int randomInt, GuessingGamesStats guessingGamesStatsObj) {
      // user guess tested against randomInt
      int guess;
      
      Scanner inputObj = new Scanner(System.in);

      do {
         System.out.print("Your guess? ");
         
         try {
            guess = Integer.parseInt(inputObj.nextLine());
            playGameWhilePrint(guess, randomInt);
            guessingGamesStatsObj.incrementNumTries();
            
         } catch (NumberFormatException e) {
            guess = randomInt + 1;
            System.out.println("Please input a number.");
         
         } // end of try-catch

      } while (randomInt != guess); // end of while
      return;

   } // end of playGameWhile

   public static void playGameWhilePrint(int guess, int randomInt) {
      if (guess > randomInt) {
         System.out.println("It's lower.");

      } else if (guess < randomInt) {
         System.out.println("It's higher.");

      } // end of if/else
      return;

   } // end of playGameWhilePrint

   // summary statistics of the games played are displayed to user
   public static void summary(GuessingGamesStats guessingGamesStatsObj) {
      // reused stats
      int numGames = guessingGamesStatsObj.getNumGames();
      int numTriesSum = guessingGamesStatsObj.getNumTriesSum();
      
      System.out.println("\nOverall results:");
      System.out.printf("\ttotal games   = %d%n", numGames);
      System.out.printf("\ttotal guesses = %d%n", numTriesSum);
      System.out.printf("\tguesses/game  = %.2f%n", (double) numTriesSum / numGames);
      System.out.printf("\tbest game     = %d%n", guessingGamesStatsObj.getBestGame());

      return;

   } // end of summary

} // end of GSmithGuessingGame class

class GuessingGamesStats {
   // games stats
   private int numGames;
   private int numTriesSum;
   private int bestGame;

   // game stats
   private int numTries;

   public GuessingGamesStats() {
      startNewGame();
      this.numGames -= 1;

   }

   public void startNewGame() {
      setBestGame(this.numTries);
      this.numTriesSum += this.numTries;
      this.numGames += 1;
      this.numTries = 0;

      return;
   }

   public void setBestGame(int numTries) {
      // bestGame and numTries declare as 0 so instantiation of obj
      // will have the intitial correct value for bestGame, 0
      if (this.bestGame == 0 || numTries < this.bestGame) {
         this.bestGame = numTries;

      }
      return;
   }

   public void incrementNumTries() {
      this.numTries += 1;

      return;
   }
   
   public int getNumTries() {
      return this.numTries;
      
   }
   
   public int getNumGames() {
      return this.numGames;
      
   }
   
   public int getNumTriesSum() {
      return this.numTriesSum;
      
   }
   
   public int getBestGame() {
      return this.bestGame;
      
   }

} // end of GuessingGamesStats class

class GuessingGamesNumbers {
   private int endRange;
   private int startRange;
   private int randomInt;
   private Random randomObj = new Random();

   public GuessingGamesNumbers() {
      setNewNumbers();

   }

   public void setNewNumbers() {
      // endRange sampled from [10, 100]
      this.endRange = randomObj.nextInt(91) + 10;

      // startRange sampled from [1, endRange-1]
      this.startRange = randomObj.nextInt(this.endRange - 1) + 1;

      // randomInt sampled from [startRange, endRange]
      this.randomInt = randomObj.nextInt(this.endRange - this.startRange + 1) + this.startRange;

      return;
   }

   public int getEndRange() {
      return this.endRange;

   }

   public int getStartRange() {
      return this.startRange;

   }

   public int getRandomInt() {
      return this.randomInt;

   }

} // end of GuessingGamesNumbers class