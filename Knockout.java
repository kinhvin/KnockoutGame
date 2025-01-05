/* Classname: Knockout
 * Created By: Kevin Truong
 * Last Modified: June 14, 2022
 * Description: Roll 2 dice, find the sum and see if you're knocked out! Play the game to find out more!
 * */

import java.awt.*;
import java.io.*;
import hsa.Console;

public class Knockout
{
     // Declare Console variable
     static Console c;
     
     public static void main (String[] args) throws IOException
     {       
          // Create Console object
          c = new Console();
          
          // Declare Variables
          // In any given array, index [0] is the player and index [1] is the computer
          char resume; // Stores the input to continue the game at stop points
          String proceed = ""; // Whether the player would like to proceed to the start of the game/next round
          String totalRdsChk = ""; // Used to validate the user inputted total rounds
          String a2Check = ""; // Used to validate the player choice within action 2
          String restartCheck = ""; // Used to validate the restart choice
          String replayCheck = ""; // Used to validate the player's replay choice
          int[] wins = new int[] {0, 0}; // The number of wins for each player
          int[] losses = new int[] {0, 0}; // The number of losses for each player
          int[] ties = new int[] {0, 0}; // The number of ties for each player
          int[] sums = new int[] {0, 0}; // The roll sums for each player
          int[] pts = new int[] {0, 0}; // The points of both players
          int[] knocks = new int[] {0, 0}; // The knockouts of both players
          int[] action = new int[] {0, 0}; // The actions chosen by the players
          int[] knocked = new int[] {0, 0}; // The status of being knocked out per round
          int[] rndWin = new int[] {0, 0}; // Stores the current round winner
          int[] a2Choice = new int[] {0, 0}; // Action 2 choice
          int rounds = 1; // The round # as the game plays out
          int totalRounds = 0; // The user defined amount of rounds that will be played
          int replay = 1; // Whether the player would like to play again 
          int restart = 0; // The choice to restart mid-game
          PrintWriter output; // File output object
          
          // Welcome message and explanation of game
          c.println("Welcome to Knockout!");
          c.println("--------------------------------------------------------------------------------");
          c.println("In this game, 2 dice will be rolled for each player and the sums will be \ncalculated.");
          c.println("The knockout range is 7, 8, 9, 10.");
          c.println("If a player's dice roll sum falls within that range, they will be knocked out \nfor the round.");
          c.println("This means that they will be unable to act for that round.");
          c.println("--------------------------------------------------------------------------------");
          c.println("The player that is not knocked out will be given the choice of action!");
          c.println("There are 3 choices: Roll again, Add or Deduct and Next Round.");
          c.println("Roll Again - The player will roll again and if they are not in the knockout \nrange, gain 1 point.");
          c.println("Add or Deduct - Add or Deduct 1 from the value of your opponent's roll sum next round.");
          c.println("--------------------------------------------------------------------------------");
          c.println("If both players are not knocked out, the player with a LOWER sum will be given \nchoice of action.");
          c.println("This is a player vs. computer game. \nThere are 10 rounds!");
          
          // Prompt the user to enter anything to start the game
          c.println("\nEnter anything to proceed to the game:");
          resume = c.getChar();
          
          // Clear the screen
          c.clear();
          
          // Loop as long as the player wants to play again
          while (replay == 1)
          {
               // Open the output file
               output = new PrintWriter (new FileWriter ("output.txt")); // open output file
               
               // Reset the restart value
               restart = 0;
               
               // Prompt the user to enter how many rounds they would like to play
               c.println("Enter the amount of rounds you would like to play (Max 10):");
               
               // Validate the user entry
               while(true)
               {
                    try
                    {
                         totalRdsChk = c.readLine();
                         totalRounds = Integer.parseInt(totalRdsChk);
                         
                         // Check the range
                         if(totalRounds < 11 && totalRounds > 0)
                              break;
                         else
                              c.println("That is not a valid entry. Please try again with a number from 1 to 10.");
                    } // end of try
                    
                    catch(NumberFormatException e)
                    {
                         c.println("That is not a valid entry. Please enter a number.");
                    } // end of catch
                    
               } // end of data validation loop
               
               // Clear the screen
               c.clear();
               
               // Main game looping until the user defined amount of rounds are played out
               while (rounds <= totalRounds)
               {
                    // Add or deduct from the opponents roll sums if applicable
                    // Player 1
                    p1Action2(a2Choice, sums);
                    
                    // Computer
                    pcAction2(a2Choice, sums);
                    
                    // Reset action 2 choices
                    a2Choice[0] = 0;
                    a2Choice[1] = 0;
                    
                    // Output round #
                    c.println("Round " + rounds);
                    
                    // Roll 2 dice and find the sum for the player
                    sums[0] = roll() + roll();
                    c.println("\nPlayer's roll sum: " + sums[0]);
                    
                    // See if the player will be knocked out
                    if (sums[0] < 7 || sums[0] > 10)
                    {
                         c.println("The player is safe!");
                    } // end of if
                    
                    else
                    {
                         c.println("The player is knocked out for the round...");
                         knocked[0] = knocked[0] + 1;
                         knocks[0] = knocks[0] + 1;
                    } // end of else
                    
                    // Roll 2 dice and find the sum for the computer
                    sums[1] = roll() + roll();
                    c.println("\nComputer's roll sum: " + sums[1]);
                    
                    // See if the computer will be knocked out
                    if (sums[1] < 7 || sums[1] > 10)
                    {
                         c.println("The computer is safe!");
                    } // end of if
                    
                    else
                    {
                         c.println("The computer is knocked out for the round...");
                         knocked[1] = knocked[1] + 1;
                         knocks[1] = knocks[1] + 1;
                    } // end of else
                    
                    // Determine the round winner
                    // Player knocked, computer not knocked
                    if (knocked[0] == 1 && knocked[1] == 0)
                    {
                         c.println("\nThe computer has won this round!");
                         c.println("The computer has been awarded 2 points.");
                         rndWin[1] = 1;
                         wins[1] = wins[1] + 1;
                         pts[1] = pts[1] + 2;
                         losses[0] = losses[0] + 1;
                    } // end of if
                    
                    // Player not knocked, computer knocked
                    else if (knocked[0] == 0 && knocked[1] == 1)
                    {
                         c.println("\nThe player has won this round!");
                         c.println("The player has been awarded 2 points.");
                         rndWin[0] = 1; // Temporary round win goes to player 1
                         wins[0] = wins[0] + 1; // Player 1 gets a win
                         pts[0] = pts[0] + 2; // Player 1 gets 2 points
                         losses[1] = losses[1] + 1; // The computer gets a loss
                    } // end of else if
                    
                    // Both players knocked
                    else if (knocked[0] == 1 && knocked[1] == 1)
                    {
                         c.println("\nBoth players are knocked out...");
                         c.println("It is an automatic tie.");
                         c.println("Each player gains 1 pity point!");
                         ties[0] = ties[0] + 1;
                         ties[1] = ties[1] + 1;
                         pts[0] = pts[0] + 1;
                         pts[1] = pts[1] + 1;
                    } // end of else if
                    
                    // Neither player is knocked out
                    else if (knocked[0] == 0 && knocked[1] == 0)
                    {
                         c.println("\nNeither player is knocked out...");
                         
                         // Determine the player with a lower roll sum
                         if (sums[0] > sums[1])
                         {
                              c.println("The computer has a lower roll sum and has won this round!");
                              c.println("The computer has been awarded 2 points.");
                              rndWin[1] = 1;
                              wins[1] = wins[1] + 1;
                              pts[1] = pts[1] + 2;
                              losses[0] = losses[0] + 1; // Player 1 gains a loss
                         } // end of if
                         
                         else if (sums[0] < sums[1])
                         {
                              c.println("The player has a lower roll sum and has won this round!");
                              c.println("The player has been awarded 2 points.");
                              rndWin[0] = 1;
                              wins[0] = wins[0] + 1;
                              pts[0] = pts[0] + 2;
                              losses[1] = losses[1] + 1; // Computer gains a loss
                         } // end of else if
                         
                         else if (sums[0] == sums[1])
                         {
                              c.println("It is a tie!");
                              c.println("Each player gains 1 point.");
                              pts[0] = pts[0] + 1;
                              pts[1] = pts[1] + 1;
                              ties[0] = ties[0] + 1;
                              ties[1] = ties[1] + 1;
                         } // end of else if
                         
                    } // end of else if
                    
                    // Reset roll sum value of the player
                    sums[0] = 0;
                    
                    // Allow the player to choose their action if they won
                    if (rndWin[0] == 1)
                    {
                         action[0] = chooseActionPlayer();
                    } // end of if
                    
                    // Action 1 / P1
                    if (action[0] == 1) // Roll again
                    {
                         c.println("\nThe player is rolling again!");
                         sums[0] = roll() + roll();
                         c.println("Roll sum: " + sums[0]);
                         
                         if (sums[0] < 7 || sums[0] > 10)
                         {
                              c.println("The player wasn't in the knockout range again!");
                              c.println("Take 1 more point!");
                              pts[0] = pts[0] + 1;
                         } // end of if
                         
                         else
                         {
                              c.println("The player was within the knockout range and gains nothing...");
                         } // end of else
                         
                    } // end of if
                    
                    // Action 2 / P1
                    if (action[0] == 2)
                    {
                         c.println("1 - Add 1 to opponents next roll sum");
                         c.println("2 - Subtract 1 from opponents next roll sum");
                         
                         // Validate that the user entry is within the menu range
                         while(true)
                         {
                              try
                              {
                                   a2Check = c.readLine();
                                   a2Choice[0] = Integer.parseInt(a2Check);
                                   
                                   // Check the range
                                   if(a2Choice[0] < 3 && a2Choice[0] > 0)
                                        break;
                                   else
                                        c.println("That is not a valid entry. Please try again with a number from 1 to 2.");
                              } // end of try
                              
                              catch(NumberFormatException e)
                              {
                                   c.println("That is not a valid entry. Please enter a number.");
                              } // end of catch
                              
                         } // end of data validation loop
                         
                    } // end of if
                    
                    // Reset roll sum of computer
                    sums[1] = 0;
                    
                    // The computer randomly chooses an action if they are the winner
                    if (rndWin[1] == 1)
                    {
                         action[1] = chooseActionComputer();
                         c.println(action[1]);
                    } // end of if
                    
                    // Action 1 / PC
                    if (action[1] == 1) // Roll again
                    {
                         c.println("\nThe computer is rolling again!");
                         sums[1] = roll() + roll();
                         c.println("Roll sum: " + sums[1]);
                         
                         if (sums[1] < 7 || sums[1] > 10)
                         {
                              c.println("The computer wasn't in the knockout range again!");
                              c.println("Take 1 more point!");
                              pts[1] = pts[1] + 1;
                         } // end of if
                         
                         else
                         {
                              c.println("The computer was within the knockout range and gains nothing...");
                         } // end of else
                         
                    } // end of if
                    
                    // Action 2 / PC
                    if (action[1] == 2)
                    {
                         c.println("1 - Add 1 to opponents next roll sum");
                         c.println("2 - Subtract 1 from opponents next roll sum");
                         a2Choice[1] = (int)(Math.random( )*2 +1);
                         c.println(a2Choice[1]);
                    } // end of if
                    
                    // Action 3
                    // Do nothing
                    
                    // Prompt user to enter anything to see the stats
                    c.println("\nEnter anything to continue to the stats screen:");
                    resume = c.getChar();
                    
                    // Clear the screen
                    c.clear();
                    
                    // Show the current player stats
                    outputStats (totalRounds, rounds, pts, knocks, wins, losses, ties);
                    
                    // Reset the action choices
                    action[0] = 0;
                    action[1] = 0;
                    
                    // Reset the roll sums
                    sums[0] = 0;
                    sums[1] = 0;
                    
                    // Reset the round winner and knocked values
                    knocked[0] = 0;
                    knocked[1] = 0;
                    rndWin[0] = 0;
                    rndWin[1] = 0;
                    
                    // Ask the player if they would like to proceed
                    c.println("\nEnter anything to continue to the next round (x to exit):");
                    proceed = c.readLine();
                    
                    if(proceed.equals("x"))
                    {
                         // Ask the player if they would like to play again
                         c.println("\n1 - Restart");
                         c.println("2 - Exit Game");
                         
                         // Validate that the user entry is within the menu range
                         while(true)
                         {
                              try
                              {
                                   restartCheck = c.readLine();
                                   restart = Integer.parseInt(restartCheck);
                                   
                                   // Check the range
                                   if(restart < 3 && restart > 0)
                                        break;
                                   else
                                        c.println("That is not a valid entry. Please try again with a number from 1 to 2.");
                              } // end of try
                              
                              catch(NumberFormatException e)
                              {
                                   c.println("That is not a valid entry. Please enter a number.");
                              } // end of catch
                              
                         } // end of data validation loop
                         
                         // Restart the game
                         if (restart == 1)
                         {
                              // Reset all the necessary values
                              rounds = 0;
                              totalRounds = 0;
                              pts[0] = 0;
                              pts[1] = 0;
                              knocks[0] = 0;
                              knocks[1] = 0;
                              wins[0] = 0;
                              wins[1] = 0;
                              losses[0] = 0;
                              losses[1] = 0;
                              ties[0] = 0;
                              ties[1] = 0;
                              sums[0] = 0;
                              sums[1] = 0;
                         } // end of if
                         
                         // Exit the game
                         else if (restart == 2)
                         {
                              System.exit(0);
                         } // end of else if
                         
                    } // end of if
                    
                    // Clear the screen
                    c.clear();
                    
                    // Add 1 to the rounds
                    rounds++;
                    
                    // Close the output file
                    output.close ();  // output file
                    
               } // end of game loop
               
               // Runs as long as a game restart is not taking place
               if (restart != 1)
               {
                    // Console output
                    outputStats (totalRounds, rounds, pts, knocks, wins, losses, ties);
                    
                    // Determine the GAME winner
                    outputResults(pts);
                    
                    // Ask the player if they would like to play again
                    c.println("1 - Play Again");
                    c.println("2 - Exit Game");
                    
                    // Validate that the user entry is within the menu range
                    while(true)
                    {
                         try
                         {
                              replayCheck = c.readLine();
                              replay = Integer.parseInt(replayCheck);
                              
                              // Check the range
                              if(replay < 3 && replay > 0)
                                   break;
                              else
                                   c.println("That is not a valid entry. Please try again with a number from 1 to 2.");
                         } // end of try
                         
                         catch(NumberFormatException e)
                         {
                              c.println("That is not a valid entry. Please enter a number.");
                         } // end of catch
                         
                    } // end of data validation loop
                    
                    // Replay the game
                    if (replay == 1)
                    {
                         // Reset all necessry values
                         rounds = 1;
                         totalRounds = 0;
                         pts[0] = 0;
                         pts[1] = 0;
                         knocks[0] = 0;
                         knocks[1] = 0;
                         wins[0] = 0;
                         wins[1] = 0;
                         losses[0] = 0;
                         losses[1] = 0;
                         ties[0] = 0;
                         ties[1] = 0;
                         sums[0] = 0;
                         sums[1] = 0;
                    } // end of if
                    
                    // Exit the game
                    else if (replay == 2)
                    {
                         System.exit(0);
                    } // end of else if
                    
                    // Clear the screen
                    c.clear();
                    
               } // end of if
               
          } // end of while
          
     }//end of main
     
     /*********************************************Methods*********************************************/
     public static int roll()
     {
          // Generate a random number between 1 and 6
          int diceRoll = (int)(Math.random( )*6 +1);
          
          // Return the generated value
          return(diceRoll);
          
     } // end of roll() method
     
     public static int chooseActionPlayer()
     {
          // Variables
          String actCheck; // Used to validate the user input
          int act = 0; // The chosen action
          
          // Prompt the player to select an action
          c.println("\nPlayer - Select your action:");
          c.println("1 - Roll again");
          c.println("2 - Add or Deduct");
          c.println("3 - Next round");
          
          // Validate the user input is within the menu range
          while(true)
          {
               try
               {
                    actCheck = c.readLine();
                    act = Integer.parseInt(actCheck);
                    
                    // Check the range
                    if(act < 4 && act > 0)
                         break;
                    else
                         c.println("That is not a valid entry. Please try again with a number from 1 to 3.");
               } // end of try
               
               catch(NumberFormatException e)
               {
                    c.println("That is not a valid entry. Please enter a number.");
               } // end of catch
               
          } // end of data validation loop
          
          // Return the chosen action
          return(act);
          
     } // end of chooseActionP1
     
     public static int chooseActionComputer()
     {
          c.println("\nComputer - Select your action:");
          c.println("1 - Roll again");
          c.println("2 - Add or Deduct");
          c.println("3 - Next round");
          
          // Generate a random number as the computer's choice
          int act = (int)(Math.random( )*3 +1);
          
          // Return the random number
          return(act);
          
     } // end of chooseActionPc
     
     public static void p1Action2 (int act2[], int rollSum[])
     {
          // Add or deduct from the opponents roll sum
          // Player
          if (act2[0] == 1) // Add 1 to the computer's roll sum
          {
               rollSum[1] = rollSum[1] + 1;
          } // end of if
          
          else if (act2[0] == 2) // Subtract 1 from the computer's roll sum
          {
               rollSum[1] = rollSum[1] - 1;
          } // end of else if
          
     } // end of p1Action2() command method
     
     public static void pcAction2 (int act2[], int rollSum[])
     {
          // Add or deduct from the opponents roll sum
          // Computer
          if (act2[1] == 1) // Add 1 to the player's roll sum
          {
               rollSum[0] = rollSum[0] + 1;
          } // end of if
          
          else if (act2[1] == 2) // Subtract 1 from the player's roll sum
          {
               rollSum[0] = rollSum[0] - 1;
          } // end of else if
          
     } // end of pcAction2() command method
     
     public static void outputStats (int totalRnds, int round, int points[], int knockouts[], int victories[], int defeats[], int tied[]) throws IOException
     {
          // Open the output file
          PrintWriter output = new PrintWriter(new FileWriter("output.txt", true));
          
          // Outputs as long as the game is still playing out
          if(round <= totalRnds)
          {
               // Output the player stats to the console
               c.println("Round " + round); // console
               c.println("\nPlayer");
               c.println("Points: " + points[0]);
               c.println("Knockouts: " + knockouts[0]);
               c.println("Wins: " + victories[0]);
               c.println("Losses: " + defeats[0]);
               c.println("Ties: " + tied[0]);
               
               // Output the computer stats to the console
               c.println("\nComputer");
               c.println("Points: " + points[1]);
               c.println("Knockouts: " + knockouts[1]);
               c.println("Wins: " + victories[1]);
               c.println("Losses: " + defeats[1]);
               c.println("Ties: " + tied[1]);
               
               // Output the player stats to the output file
               output.println("Round " + round);
               output.println("\nPlayer");
               output.println("Points: " + points[0]);
               output.println("Knockouts: " + knockouts[0]);
               output.println("Wins: " + victories[0]);
               output.println("Losses: " + defeats[0]);
               output.println("Ties: " + tied[0]);
               
               // Output the computer stats to the output file
               output.println("\nComputer");
               output.println("Points: " + points[1]);
               output.println("Knockouts: " + knockouts[1]);
               output.println("Wins: " + victories[1]);
               output.println("Losses: " + defeats[1]);
               output.println("Ties: " + tied[1] + "\n");
          } // end of if
          
          // The stats outputted at the end of the game
          else
          {
               // Output the final player stats to the console
               c.println("End Game Stats");
               c.println("\nPlayer");
               c.println("Points: " + points[0]);
               c.println("Knockouts: " + knockouts[0]);
               c.println("Wins: " + victories[0]);
               c.println("Losses: " + defeats[0]);
               c.println("Ties: " + tied[0]);
               
               // Output the final computer stats to the console
               c.println("\nComputer");
               c.println("Points: " + points[1]);
               c.println("Knockouts: " + knockouts[1]);
               c.println("Wins: " + victories[1]);
               c.println("Losses: " + defeats[1]);
               c.println("Ties: " + tied[1]);
               
               // Output the final player stats to the output file
               output.println("\nEnd Game Stats");
               output.println("\nPlayer");
               output.println("Points: " + points[0]);
               output.println("Knockouts: " + knockouts[0]);
               output.println("Wins: " + victories[0]);
               output.println("Losses: " + defeats[0]);
               output.println("Ties: " + tied[0]);
               
               // Output the final computer stats to the output file
               output.println("\nComputer");
               output.println("Points: " + points[1]);
               output.println("Knockouts: " + knockouts[1]);
               output.println("Wins: " + victories[1]);
               output.println("Losses: " + defeats[1]);
               output.println("Ties: " + tied[1] + "\n");
               
          } // end of else
          
          // Close the output file
          output.close();  // output file
          
     } // end of outputStats() method
     
     public static void outputResults (int points[]) throws IOException
     {
          // Open the output file
          PrintWriter output = new PrintWriter(new FileWriter("output.txt", true));
          
          // Determine the GAME winner
          if (points[0] > points[1])
          {
               c.println("\nThe player has won the game!\n");
               output.println("The player has won the game!");
          } // end of if
          
          else if (points[0] < points[1])
          {
               c.println("\nThe computer has won the game!\n");
               output.println("The computer has won the game!");
          } // end of else if
          
          else if (points[0] == points[1])
          {
               c.println("\nIt is a tie...");
               
               // Prompt the user to choose OVERTIME or ending as a tie
               c.println("\nYou have the choice to play OVERTIME!");
               c.println("In OVERTIME, you will play a game of rock, paper, scissors to decide the winner!");
               c.println("\n1 - Go into OVERTIME");
               c.println("2 - End as a tie");
               
               // OVERTIME variables
               int overtimeChoice = 0; // The user's choice to play overtime or not
               String overtimeCheck = ""; // Used to validate the user's input
               
               // Validate that the user entry is within the menu range
               while(true)
               {
                    try
                    {
                         overtimeCheck = c.readLine();
                         overtimeChoice = Integer.parseInt(overtimeCheck);
                         
                         // Check the range
                         if(overtimeChoice < 3 && overtimeChoice > 0)
                              break;
                         else
                              c.println("That is not a valid entry. Please try again with a number from 1 to 2.");
                    } // end of try
                    
                    catch(NumberFormatException e)
                    {
                         c.println("That is not a valid entry. Please enter a number.");
                    } // end of catch
                    
               } // end of data validation loop
               
               // Clear the screen
               c.clear();
               
               // OVERTIME VARIABLES (rock, paper, scissors)
               int playerState = 0; // The state of the player
               int compState = 0; // The state of the computer
               
               // Player states
               int rock1 = 1;
               int paper1 = 2;
               int scissors1 = 3;
               
               // Computer states
               int rock2 = 1;
               int paper2 = 2;
               int scissors2 = 3;
               
               // Other
               int winner = 0; // Used to store when a winner is decided
               char cont; // Continue the game
               
               // Play rock, paper, scissors for OVERTIME
               if (overtimeChoice == 1)
               {
                    // Keep playing until there is a clear winner
                    while (winner != 1)
                    {
                         // Let the player choose their state
                         c.println("Player - Choose your state");
                         c.println("1 - Rock");
                         c.println("2 - Paper");
                         c.println("3 - Scissors");
                         playerState = c.readInt();
                         
                         // Randomly generate the state of the computer
                         c.println("\nComputer - Choose your state");
                         c.println("1 - Rock");
                         c.println("2 - Paper");
                         c.println("3 - Scissors");
                         compState = (int)(Math.random( )*3 + 1);
                         c.println(compState + "\n");
                         
                         // Determine the state of the player
                         if(playerState == 1)
                         {
                              playerState = rock1;
                              c.println("The player is rock...");
                         } // end of if
                         
                         else if(playerState == 2)
                         {
                              playerState = paper1;
                              c.println("The player is paper...");
                         } // end of else if
                         
                         else
                         {
                              playerState = scissors1;
                              c.println("The player is scissors...");
                         } // end of else
                         
                         // Determine the state of the computer
                         if(compState == 1)
                         {
                              compState = rock2;
                              c.println("The computer is rock...");
                         } // end of if
                         
                         else if(compState == 2)
                         {
                              compState = paper2;
                              c.println("The computer is paper...");
                         } // end of else if
                         
                         else
                         {
                              compState = scissors2;
                              c.println("The computer is scissors...");
                         } // end of else
                         
                         // Determine the winner
                         if(playerState == compState)
                         {
                              c.println("\nIt is a tie...");
                         } // end of if
                         
                         // Rock outcomes
                         if (playerState == rock1 && compState == paper2)
                         {                    
                              c.println("\nPaper beats rock!");
                              c.println("\nThe computer has won the game!");
                              output.println("Tiebreaker Result: The computer has won the game!");
                              winner = winner + 1;
                         } // end of if
                         
                         else if (playerState == rock1 && compState == scissors2)
                         {
                              c.println("\nRock beats scissors!");
                              c.println("\nThe player has won the game!");
                              output.println("Tiebreaker Result: The player has won the game!");
                              winner = winner + 1;
                         } // end of else if
                         
                         if (compState == rock2 && playerState == paper1)
                         {
                              c.println("\nPaper beats rock!");
                              c.println("\nThe player has won the game!");
                              output.println("The player has won the game!");
                              winner = winner + 1;
                         } // end of if
                         
                         else if (compState == rock2 && playerState == scissors1)
                         {
                              c.println("\nRock beats scissors!");
                              c.println("\nThe computer has won the game!");
                              output.println("Tiebreaker Result: The computer has won the game!");
                              winner = winner + 1;
                         } // end of else if
                         
                         // Paper outcomes
                         if (playerState == paper1 && compState == scissors2)
                         {
                              c.println("\nScissors beats paper!");
                              c.println("\nThe computer has won the game!");
                              output.println("Tiebreaker Result: The computer has won the game!");
                              winner = winner + 1;
                         } // end of if
                         
                         if (compState == paper2 && playerState == scissors1)
                         {
                              c.println("\nScissors beats paper!");
                              c.println("\nThe player has won the game!");
                              output.println("Tiebreaker Result: The player has won the game!");
                              winner = winner + 1;
                         } // end of if
                         
                         // Prompt for user to continue
                         c.println("\nEnter anything to continue:");
                         cont = c.getChar();
                         
                         // Clear the screen
                         c.clear();
                         
                    } // end of rock, paper, scissors game loop
                    
               } // end of if
               
               else if (overtimeChoice == 2)
               {
                    output.println("It is a tie...");
               } // end of else if
               
          } // end of OVERTIME choice else if
          
          // Close the output file
          output.close();  // output file
          
     } // end of outputResults() command method
     
}//end of class