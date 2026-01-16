package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.Player;
import view.BohnanzaGameFrame;

/**
 * @author Mohan Dixit(90%), Saran Rajagopal(10%) -> all the TitleFrame listener
 *         code
 **/

public class SurfaceController implements ActionListener, MouseListener {

	ArrayList<String> gameLog = new ArrayList<String>();

	public static BohnanzaGameFrame gameFrame;

	public static AreaController areaController;
	public static PlayerController playerController;
	public static int difficultyChoice;
	public static boolean onePlayer;

	private HardAIController hardAI;
	private MediumAIController mediumAI;
	private EasyAIController easyAI;

	private static int gameState = 1;
	private static final String[] gameStates = { "ClearOfferArea", // State 0
			"PlantAndDiscard", // State 1
			"DrawOfferAreaCards", // State 2
			"DrawCards" // State 3
	};

	public SurfaceController() {
		initializeSurface();
	}

	public void initializeSurface() {
		gameFrame = new BohnanzaGameFrame();
		areaController = new AreaController();
		playerController = new PlayerController();

		gameFrame.getHelpPanel().button2.addActionListener(this);
		gameFrame.getHelpPanel().button4.addActionListener(this);
		gameFrame.getHelpPanel().Helpbutton.addActionListener(this);
		gameFrame.getHelpPanel().button3.addActionListener(this);
		gameFrame.getOptionPanel().onePlayerButton.addActionListener(this);
		gameFrame.getOptionPanel().twoPlayerButton.addActionListener(this);
		gameFrame.getOptionPanel().instructionButton.addActionListener(this);
		gameFrame.aiDifficultyPanel.easyButton.addActionListener(this);
		gameFrame.aiDifficultyPanel.mediumButton.addActionListener(this);
		gameFrame.aiDifficultyPanel.hardButton.addActionListener(this);

		gameFrame.titlepagelabel.addMouseListener(this);

		gameFrame.setVisible(true);
	}

	public boolean checkGameEnd() {
		return areaController.isDeckEmpty();
	}

	public Player displayWinner() {
		return playerController.displayWinner();
	}

	public void resetSurface() {
		gameFrame.dispose();
		gameFrame = new BohnanzaGameFrame();
	}

	public static int getGameState() {
		return gameState;
	}

	public static void setGameState(int gameState) {
		SurfaceController.gameState = gameState;
	}

	public static String[] getGamestates() {
		return gameStates;
	}

	public static String setGameStateText(int gameState) {
		if (gameStates[gameState] == "ClearOfferArea") {
			return "Clear OfferArea";
		} else if (gameStates[gameState] == "PlantAndDiscard") {
			return "Plant & Discard";
		} else if (gameStates[gameState] == "DrawOfferAreaCards") {
			return "Draw OfferArea Cards";
		} else if (gameStates[gameState] == "DrawCards") {
			return "Draw Cards";
		}
		return gameStates[gameState];
	}

	public void startGame() {
		gameState = 1;

		gameFrame.currentStep.setText(setGameStateText(gameState));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		gameFrame.dispose();
		gameFrame.Settings();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == gameFrame.getOptionPanel().onePlayerButton) {
			onePlayer = true;
			gameFrame.AIDifficultyPage();
		}

		if (e.getSource() == gameFrame.aiDifficultyPanel.easyButton) {
			difficultyChoice = 0;
			easyAI = new EasyAIController(playerController.player2);

			gameFrame.StartGame();
			startGame();

			if (easyAI.aiPlayer.equals(playerController.currentPlayer)) {
				easyAI.takeTurn();
				gameState = 0;
				playerController.switchPlayer();
				gameFrame.currentStep.setText(setGameStateText(gameState));
			}
		}

		if (e.getSource() == gameFrame.aiDifficultyPanel.mediumButton) {
			difficultyChoice = 1;
			mediumAI = new MediumAIController(playerController.player2);

			gameFrame.StartGame();
			startGame();

			if (mediumAI.aiPlayer.equals(playerController.currentPlayer)) {
				mediumAI.takeTurn();
				gameState = 0;
				playerController.switchPlayer();
				gameFrame.currentStep.setText(setGameStateText(gameState));
			}
		}

		if (e.getSource() == gameFrame.aiDifficultyPanel.hardButton) {
			difficultyChoice = 2;
			hardAI = new HardAIController(playerController.player2);

			gameFrame.StartGame();
			startGame();

			if (hardAI.aiPlayer.equals(playerController.currentPlayer)) {
				hardAI.takeTurn();
				gameState = 0;
				playerController.switchPlayer();
				gameFrame.currentStep.setText(setGameStateText(gameState));
			}
		}

		if (e.getSource() == gameFrame.getOptionPanel().twoPlayerButton) {
			onePlayer = false;
			gameFrame.StartGame();
			startGame();
		}

		if (e.getSource() == gameFrame.getOptionPanel().instructionButton) {
			gameFrame.showTextFilePopup();
		}

		if (e.getSource() == gameFrame.getHelpPanel().button3) {

			int choice = JOptionPane.showConfirmDialog(gameFrame,
					"Are you sure you want to exit this game?\nIf you do, you will need to start a new game.",
					"Confirm Exit", JOptionPane.YES_NO_OPTION);

			if (choice == JOptionPane.YES_OPTION) {
				gameFrame.dispose();
				initializeSurface();
			}
		}

		if (e.getSource() == gameFrame.getHelpPanel().Helpbutton) {

			if (gameState == 1) {
				JOptionPane.showMessageDialog(gameFrame,
						"Plant your first card, then if you want you can plant a second card. At the end of your turn(if you want), discard a card.",
						"Help", JOptionPane.INFORMATION_MESSAGE);
			}

			if (gameState == 2) {
				JOptionPane.showMessageDialog(gameFrame,
						"The Offer Area cards have been drawn for you. Choose if you want to plant any!", "Help",
						JOptionPane.INFORMATION_MESSAGE);
			}

			if (gameState == 3) {
				JOptionPane.showMessageDialog(gameFrame,
						"Your cards have been drawn for you. See if you want to harvest any of your fields!", "Help",
						JOptionPane.INFORMATION_MESSAGE);
			}

			if (gameState == 0) {
				JOptionPane.showMessageDialog(gameFrame,
						"Clear the offer area by choosing to plant or discard the central cards!", "Help",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}

		if (e.getSource() == gameFrame.getHelpPanel().button2) {

			if (checkGameEnd()) {

				for (int i = 0; i < 3; i++) {
					playerController.sellCards(i);
				}

				playerController.switchPlayer();

				for (int i = 0; i < 3; i++) {
					playerController.sellCards(i);
				}

				playerController.updateCoinDisplay(playerController.player1);
				playerController.updateCoinDisplay(playerController.player2);

				Player winner = displayWinner();

				String winnerNumber;
				if (winner == playerController.player1) {
					winnerNumber = "1";
				} else {
					winnerNumber = "2";
				}

				JOptionPane.showMessageDialog(null,
						"Game Over! Winner: Player " + winnerNumber + " with " + winner.getCoinTotal() + " coins!",
						"Game Over", JOptionPane.INFORMATION_MESSAGE);
				resetSurface();
				return;
			}

			if (gameState == 0) {
				for (int i = 0; i < 3; i++) {
					if (areaController.getOfferArea()[i] != null) {
						JOptionPane.showMessageDialog(null, "Clear all the center cards first!", "Warning",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
			} else if (gameState == 1) {
				if (!playerController.canProgressToNextPhase()) {
					return;
				}
			}

			gameState++;

			if (gameState == 2) {
				areaController.drawOfferCards();

			} else if (gameState == 3) {
				for (int i = 0; i < 2; i++) {
					if (!areaController.isDeckEmpty()) {
						playerController.addCardToCurrentPlayer(areaController.deckController.getTopDeckCard());
					}
				}

			} else if (gameState >= 4) {
				playerController.switchPlayer();
				gameState = 0;

				if (onePlayer) {
					if (difficultyChoice == 0) {
						easyAI.takeTurn();
					} else if (difficultyChoice == 1) {
						mediumAI.takeTurn();
					} else if (difficultyChoice == 2) {
						hardAI.takeTurn();
					}
					playerController.switchPlayer();
				}

				for (int i = 0; i < 3; i++) {
					if (areaController.getOfferArea()[i] != null) {
						break;
					} else if (i == 2) {
						gameState++;
					}
				}

			}

			gameFrame.currentStep.setText(setGameStateText(gameState));
		}

		if (e.getSource() == gameFrame.getHelpPanel().button4) {

			Player currentPlayer = playerController.getCurrentPlayer();
			view.PlayerPanel currentPanel;

			if (currentPlayer == playerController.player1) {
				currentPanel = gameFrame.getPlayerPanel1();
			} else {
				currentPanel = gameFrame.getPlayerPanel2();
			}

			if (currentPanel.isThirdFieldEnabled()) {
				JOptionPane.showMessageDialog(null, "You already own the third bean field!", "Already Purchased",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (currentPlayer.getCoinTotal() < 3) {

				int currentCoins = currentPlayer.getCoinTotal();
				int need = 3 - currentCoins;

				String currentCoinText;
				if (currentCoins == 1) {
					currentCoinText = " coin";
				} else {
					currentCoinText = " coins";
				}

				String needText;
				if (need == 1) {
					needText = " coin";
				} else {
					needText = " coins";
				}

				JOptionPane.showMessageDialog(null,
						"You need 3 coins to buy the third bean field!\n\nYou currently have: " + currentCoins
								+ currentCoinText + "\nYou need: " + need + " more" + needText,
						"Insufficient Coins", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int choice = JOptionPane.showConfirmDialog(null,
					"Buy third bean field for 3 coins?\n\nCurrent coins: " + currentPlayer.getCoinTotal()
							+ "\nCoins after purchase: " + (currentPlayer.getCoinTotal() - 3),
					"Confirm Purchase", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (choice == JOptionPane.YES_OPTION) {

				currentPlayer.setCoinTotal(currentPlayer.getCoinTotal() - 3);

				currentPanel.enableThirdField();

				playerController.updateCoinDisplay(currentPlayer);

				JOptionPane.showMessageDialog(null,
						"Third bean field purchased successfully!\n\nRemaining coins: " + currentPlayer.getCoinTotal(),
						"Purchase Complete", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}