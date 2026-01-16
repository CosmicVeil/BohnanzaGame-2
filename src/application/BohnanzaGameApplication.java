package application;

import controller.SurfaceController;

/*
 * Name(s): 
Mohan Dixit(34%), Saran Rajagopal(33%), Vishal Parvannan(33%) 
Mohan: Surface Controller for turns + setting up game + Player class + Hard AI + Area Controller + Deck Controllers
Saran : GUIs (Gameframe + all GUI panels)  + Surface Controller for title frame + helped with Easy AI + Card class
Vishal : PlayerController + MediumAI + helped with easy AI + helped with GUI

Date:
2025-11-22

Course Code:
ICS4U1-01, Mr.Fernandes

Title:
Bohnanza game

Description:
This project is an online game version of the card game Bohnanza. However, it is modified to support only two players, whereas the original card game is typically played with multiple players. When the game starts, the user can choose to play against a friend or an AI with three difficulty levels. In the game, players can plant, discard, draw, and harvest beans by clicking on the bean cards. A current step label shows which step the player is on, and if the player needs additional help, they can press the Help button, which displays a specific message based on the current step. At the end of the game, the computer declares the winner based on the number of coins each player has, or in the case of a tie, the number of bean cards.

Features:
AI Difficulty System (Easy, Medium, Hard)
Easy system uses a random decision approach towards making a decision
Medium system uses a simplistic board view approach towards making a decision
Hard systems use expected value in relation to their fields, the offer area, the specific card, the cards remaining, and the opponents hand. Simple simulations are also used to generate the best decision
Proper implementation of the order of phase changes. Phases are skipped if any activity for the user in the phase is complete. Ex: If the offer area is empty, the clear offer area phase is skipped. Phase specific messages are provided at each phase of the game.
Planting and discarding logic implemented with respect to the two player rules
Clearing offer area is implemented with respect to the two player rules
Drawing two cards from the deck is implemented using a stack system
Discarding cards is also used through a stack system to implement Last In First out functionality
Drawing cards from the deck to the offer area is implemented correctly, with special respect towards similar cards from the discard pile.
Ending logic is correctly implemented, with the fields from both players being auto harvested at the end. Winning logic is implemented through checking player’s coins, and if tied, the number of their cards in hand.
GUI is clean and attractive while still providing immense functionality to aid the user in the game. Instructions are provided at the start of the game through scroll panels, and a “Click Anywhere” title screen is implemented to make the game feel more immersive.
User-specific messages are provided if the user makes an incorrect or illegal move, informing them of the problem and potential solutions.

Major Skills:
Arrays
Stacks
Hashmaps
Composition 
Interface
Event Handling (ActionListeners, Swing events)
Polymorphism
Model View Controller (MVC) design structure
File reader
Expected Value
OOP programming style

Areas of Concern:
None

 */

public class BohnanzaGameApplication {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new SurfaceController();
		

	}

}