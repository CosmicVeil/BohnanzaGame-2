package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Card;
import model.Player;
import view.BohnanzaGameFrame;
import view.PlayerPanel;

/**
 *@author Vishal Parvannan
 **/

public class PlayerController implements ActionListener {

	public Player player1;
	public Player player2;
	public Player startingPlayer;
	public Player currentPlayer;

	public BohnanzaGameFrame gameFrame;

	private boolean hasPlantedThisTurn = false;
	private int plantsThisTurn = 0;
	private boolean hasDiscardedThisTurn = false;

	private HashMap<JButton, Player> currentPlayerMap = new HashMap<>();
	private HashMap<JButton, Card> cardMap = new HashMap<>();
	private HashMap<JButton, Integer> fieldIndexMap = new HashMap<>();

	public PlayerController(BohnanzaGameFrame gameFrame) {
		this.gameFrame = gameFrame;

		this.player1 = new Player(initializePlayerHand());
		this.player2 = new Player(initializePlayerHand());

		this.currentPlayer = player1;

		wireBeanFieldButtonsForBothPanels();
		updatePlayersHand();
		updateCoinDisplay(currentPlayer);
	}

	public PlayerController() {
		this.player1 = new Player(initializePlayerHand());
		this.player2 = new Player(initializePlayerHand());

		if (new Random().nextBoolean()) {
			this.currentPlayer = player1;
		} else {
			this.currentPlayer = player2;
		}

		if (currentPlayer == player1) {
			SurfaceController.gameFrame.getPlayerPanel2().getStartingCard().setIcon(new ImageIcon("Blank.png"));
			SurfaceController.gameFrame.getPlayerPanel2().currentTurn.setText(" ");
		} else {
			SurfaceController.gameFrame.getPlayerPanel1().getStartingCard().setIcon(new ImageIcon("Blank.png"));
			SurfaceController.gameFrame.getPlayerPanel1().currentTurn.setText(" ");
		}

		if (SurfaceController.gameFrame != null) {
			this.gameFrame = SurfaceController.gameFrame;
			wireBeanFieldButtonsForBothPanels();
			updatePlayersHand();
			updateCoinDisplay(currentPlayer);
		}
	}

	private void wireBeanFieldButtonsForBothPanels() {
		if (SurfaceController.gameFrame == null)
			return;
		this.gameFrame = SurfaceController.gameFrame;

		PlayerPanel p1panel = gameFrame.getPlayerPanel1();
		PlayerPanel p2panel = gameFrame.getPlayerPanel2();

		JButton[] p1Fields = p1panel.getBeanFields();
		JButton[] p2Fields = p2panel.getBeanFields();

		if (p1Fields != null) {
			for (int i = 0; i < Math.min(3, p1Fields.length); i++) {
				JButton cardFieldButton = p1Fields[i];
				if (cardFieldButton != null) {
					currentPlayerMap.put(cardFieldButton, player1);
					fieldIndexMap.put(cardFieldButton, Integer.valueOf(i));
					try {
						cardFieldButton.removeActionListener(this);
					} catch (Exception ignored) {
					}
					cardFieldButton.addActionListener(this);
				}
			}
		}

		if (p2Fields != null) {
			for (int i = 0; i < Math.min(3, p2Fields.length); i++) {
				JButton cardFieldButton = p2Fields[i];
				if (cardFieldButton != null) {
					currentPlayerMap.put(cardFieldButton, player2);
					fieldIndexMap.put(cardFieldButton, Integer.valueOf(i));
					try {
						cardFieldButton.removeActionListener(this);
					} catch (Exception ignored) {
					}
					cardFieldButton.addActionListener(this);
				}
			}
		}
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Player player) {
		this.currentPlayer = player;
		if (gameFrame != null) {
			if (player == player1) {
				gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel1());
			} else {
				gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel2());
			}
		}
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player p) {
		this.player1 = p;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player p) {
		this.player2 = p;
	}

	public void switchPlayer() {
		if (currentPlayer == player1) {
			currentPlayer = player2;
			gameFrame.getPlayerPanel1().currentTurn.setText(" ");
			gameFrame.getPlayerPanel2().currentTurn.setText("Current turn");
		} else {
			currentPlayer = player1;
			gameFrame.getPlayerPanel2().currentTurn.setText(" ");
			gameFrame.getPlayerPanel1().currentTurn.setText("Current turn");
		}

		setHasPlantedThisTurn(false);
		plantsThisTurn = 0;
		setHasDiscardedThisTurn(false);

		if (gameFrame != null) {
			if (currentPlayer == player1) {
				gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel1());
			} else {
				gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel2());
			}
		}

		updatePlayersHand();
		updateCoinDisplay(currentPlayer);

	}

	public ArrayList<Card> addCardToHand(Card card) {
		currentPlayer.getCardsInHand().add(card);
		updatePlayersHand();
		return currentPlayer.getCardsInHand();
	}

	public void addCardToCurrentPlayer(Card card) {
		currentPlayer.getCardsInHand().add(card);
		updatePlayersHand();
	}

	public void addCardToPlayer1(Card card) {
		player1.getCardsInHand().add(card);
		updatePlayersHand();
	}

	public void addCardToPlayer2(Card card) {
		player2.getCardsInHand().add(card);
		updatePlayersHand();
	}

	public Player displayWinner() {
		if (player1.getCoinTotal() > player2.getCoinTotal())
			return player1;
		if (player1.getCoinTotal() < player2.getCoinTotal())
			return player2;

		if (player1.getCardsInHand().size() >= player2.getCardsInHand().size()) {
			return player1;
		} else {
			return player2;
		}
	}

	private ArrayList<Card> initializePlayerHand() {
		ArrayList<Card> hand = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Card card = SurfaceController.areaController.deckController.getTopDeckCard();
			if (card != null)
				hand.add(card);
		}
		return hand;
	}

	public void updatePlayersHand() {
		if (SurfaceController.gameFrame == null)
			return;
		this.gameFrame = SurfaceController.gameFrame;

		JPanel p1HandPanel = gameFrame.getPlayerPanel1().getScrollPanel();
		JPanel p2HandPanel = gameFrame.getPlayerPanel2().getScrollPanel();

		p1HandPanel.removeAll();
		p2HandPanel.removeAll();

		int x = 0;
		for (Card card : player1.getCardsInHand()) {
			JButton cardButton = new JButton(card.cardView());
			makeButtonLook(cardButton);
			cardButton.setBounds(x, 0, 110, 152);
			currentPlayerMap.put(cardButton, player1);
			cardMap.put(cardButton, card);
			try {
				cardButton.removeActionListener(this);
			} catch (Exception ignored) {
			}
			cardButton.addActionListener(this);
			p1HandPanel.add(cardButton);
			x += 120;
		}
		p1HandPanel.setPreferredSize(new Dimension(Math.max(1, x), 152));

		x = 0;
		for (Card card : player2.getCardsInHand()) {
			JButton cardButton = new JButton(card.cardView());
			makeButtonLook(cardButton);
			cardButton.setBounds(x, 0, 110, 152);
			currentPlayerMap.put(cardButton, player2);
			cardMap.put(cardButton, card);
			try {
				cardButton.removeActionListener(this);
			} catch (Exception ignored) {
			}
			cardButton.addActionListener(this);
			p2HandPanel.add(cardButton);
			x += 120;
		}
		p2HandPanel.setPreferredSize(new Dimension(Math.max(1, x), 152));

		wireBeanFieldButtonsForBothPanels();
		p1HandPanel.revalidate();
		p1HandPanel.repaint();
		p2HandPanel.revalidate();
		p2HandPanel.repaint();
	}

	private void makeButtonLook(JButton b) {
		b.setOpaque(false);
		b.setContentAreaFilled(false);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
	}

	public void updateCoinDisplay(Player player) {
		if (gameFrame != null) {
			if (player == player1) {
				gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel1());
			} else {
				gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel2());
			}

			gameFrame.getCurrentPlayerPanel().showCoins(player.getCoinTotal());
		}
	}

	public void updatePlantedCards(int beanFieldIndex, Card card) {
		int numCards = currentPlayer.getNumberOfCardsInBeanField()[beanFieldIndex];

		if (gameFrame == null)
			return;
		if (currentPlayer == player1) {
			gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel1());
		} else {
			gameFrame.setCurrentPlayerPanel(gameFrame.getPlayerPanel2());
		}
		gameFrame.getCurrentPlayerPanel().addNumCards(beanFieldIndex, numCards);

		if (card == null) {
			gameFrame.getCurrentPlayerPanel().getBeanFields()[beanFieldIndex]
					.setIcon(new ImageIcon("src/images/Beanfield" + (beanFieldIndex + 1) + ".jpg"));
		} else {
			gameFrame.getCurrentPlayerPanel().getBeanFields()[beanFieldIndex].setIcon(card.cardView());
		}
	}

	public void sellCards(int beanFieldIndex) {
		int numCards = currentPlayer.getNumberOfCardsInBeanField()[beanFieldIndex];
		Card beanType = currentPlayer.getCardInBeanField()[beanFieldIndex];

		if (beanType == null || numCards == 0)
			return;

		int[] priceTable = beanType.getSellingPrice();
		int coinsEarned = 0;

		for (int i = 0; i < priceTable.length; i++) {
			if (numCards >= priceTable[i])
				coinsEarned = i + 1;
		}

		currentPlayer.setCoinTotal(currentPlayer.getCoinTotal() + coinsEarned);
		currentPlayer.getCardInBeanField()[beanFieldIndex] = null;
		currentPlayer.getNumberOfCardsInBeanField()[beanFieldIndex] = 0;

		updateCoinDisplay(currentPlayer);
		updatePlantedCards(beanFieldIndex, null);
	}

	private boolean validateTurn(Player player) {
		if (currentPlayer != player) {
			JOptionPane.showMessageDialog(null, "It's not your turn!");
			return false;
		}
		return true;
	}

	private boolean isFieldAvailable(Player currentPlayer, int fieldIndex) {
		if (fieldIndex < 0 || fieldIndex > 2)
			return false;
		if (fieldIndex < 2)
			return true;

		PlayerPanel panel;
		if (currentPlayer == player1) {
			panel = gameFrame.getPlayerPanel1();
		} else {
			panel = gameFrame.getPlayerPanel2();
		}
		return panel.isThirdFieldEnabled();
	}

	public boolean canProgressToNextPhase() {
		int state = SurfaceController.getGameState();

		if (state == 1) {
			if (plantsThisTurn < 1) {
				JOptionPane.showMessageDialog(null, "You must plant at least 1 card before continuing!");
				return false;
			}
			if (plantsThisTurn > 2) {
				JOptionPane.showMessageDialog(null, "You can only plant up to 2 cards in this phase!");
				return false;
			}
		}

		return true;
	}

	public int getPlantsThisTurn() {
		return plantsThisTurn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (!(src instanceof JButton))
			return;
		JButton btn = (JButton) src;

		Player currentPlayerFromMap = currentPlayerMap.get(btn);
		Integer fieldIdxFromMap = fieldIndexMap.get(btn);

		if (fieldIdxFromMap instanceof Integer && currentPlayerFromMap instanceof Player) {
			Player currentPlayer = currentPlayerFromMap;
			int index = fieldIdxFromMap;

			if (!validateTurn(currentPlayer))
				return;

			if (!isFieldAvailable(currentPlayer, index)) {
				JOptionPane.showMessageDialog(null,
						"This field is locked!\n\nBuy the third bean field for 3 coins to unlock it.", "Field Locked",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (!currentPlayer.canHarvest(index))
				return;

			Card bean = currentPlayer.getCardInBeanField()[index];
			int num = currentPlayer.getNumberOfCardsInBeanField()[index];

			String[] opts = new String[] { "Harvest", "Cancel" };

			String beanName;
			if (bean != null) {
				beanName = bean.getBeanName();
			} else {
				beanName = "Empty";
			}

			Icon icon;
			if (bean != null) {
				icon = bean.cardView();
			} else {
				icon = null;
			}

			int choice = JOptionPane.showOptionDialog(null, null, "Bean Field: " + beanName + " x" + num,
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, opts, opts[0]);

			if (choice == 0) {

				int fieldsToCheck;
				if (gameFrame.getCurrentPlayerPanel().isThirdFieldEnabled()) {
					fieldsToCheck = 3;
				} else {
					fieldsToCheck = 2;
				}
				boolean hasFieldWithMoreCards = false;

				for (int i = 0; i < fieldsToCheck; i++) {
					if (i != index) {
						int otherFieldCards = currentPlayer.getNumberOfCardsInBeanField()[i];
						if (otherFieldCards > num) {
							hasFieldWithMoreCards = true;
						}
					}
				}

				if ((num == 1 && hasFieldWithMoreCards)) {
					JOptionPane.showMessageDialog(null,
							"You cannot harvest a field with one card if you have another field with multiple cards.");
					return;
				}

				for (int i = 0; i < num; i++) {
					SurfaceController.areaController.deckController.setTopDiscardCard(bean);
				}

				SurfaceController.gameFrame.getOfferAreaPanel().getDiscardPile().setIcon(new ImageIcon(bean.getLink()));

				sellCards(index);
				updatePlayersHand();
			}
			return;
		}
		Player currentPlayerObj = currentPlayerMap.get(btn);
		Card clickedCard = cardMap.get(btn);

		if (!(currentPlayerObj instanceof Player) || !(clickedCard instanceof Card))
			return;

		Player currentPlayer = currentPlayerObj;
		Card card = clickedCard;

		if (!validateTurn(currentPlayer))
			return;

		int currentState = SurfaceController.getGameState();

		boolean plantPossible = (currentState == 1) && (plantsThisTurn < 2)
				&& ((card == currentPlayer.getCardsInHand().get(0)) && (!hasDiscardedThisTurn));
		boolean discardPossible = (currentState == 1) && (plantsThisTurn >= 1)
				&& currentPlayer.getCardsInHand().contains(card) && (!hasDiscardedThisTurn);

		ArrayList<String> options = new ArrayList<>();

		if (plantPossible)
			options.add("Plant");
		if (discardPossible)
			options.add("Discard");

		if (options.isEmpty()) {
			if (currentState != 1) {
				JOptionPane.showMessageDialog(null,
						"You can only plant/discard during the Plant phase!\nCurrent phase: "
								+ SurfaceController.getGamestates()[currentState]);
			} else if (plantsThisTurn >= 2 && hasDiscardedThisTurn) {
				JOptionPane.showMessageDialog(null, "You have already discarded a card and planted 2 cards.");
			} else {
				JOptionPane.showMessageDialog(null, "No actions available for this card.");
			}
			return;
		}

		String[] choices = options.toArray(new String[0]);
		int choice = JOptionPane.showOptionDialog(null, "Choose action for this card:", card.getBeanName(),
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, card.cardView(), choices, choices[0]);

		if (choice == -1)
			return;

		String action = choices[choice];

		switch (action) {

		case "Plant":
			boolean planted = false;
			int plantedToField = -1;

			for (int i = 0; i < 2; i++) {
				Card existing = currentPlayer.getCardInBeanField()[i];
				if (existing != null && existing.getBeanName().equals(card.getBeanName())) {
					plantedToField = i;
					planted = true;
					break;
				}
			}

			if (!planted) {
				if (gameFrame.getCurrentPlayerPanel().isThirdFieldEnabled() && isFieldAvailable(currentPlayer, 2)) {
					Card existing = currentPlayer.getCardInBeanField()[2];
					if (existing != null && existing.getBeanName().equals(card.getBeanName())) {
						plantedToField = 2;
						planted = true;
					}
				}
			}

			if (!planted) {

				for (int i = 0; i < 2; i++) {
					if (currentPlayer.getCardInBeanField()[i] == null) {
						plantedToField = i;
						planted = true;
						break;
					}
				}

				if (!planted) {

					if (gameFrame.getCurrentPlayerPanel().isThirdFieldEnabled() && isFieldAvailable(currentPlayer, 2)) {
						if (currentPlayer.getCardInBeanField()[2] == null) {
							plantedToField = 2;
							planted = true;
						}
					}
				}
			}

			if (planted && plantedToField >= 0) {
				if (currentPlayer.getCardInBeanField()[plantedToField] == null) {
					currentPlayer.getCardInBeanField()[plantedToField] = card;
				}

				currentPlayer.addCardsToBeanField(plantedToField, 1);
				currentPlayer.getCardsInHand().remove(card);

				plantsThisTurn++;
				setHasPlantedThisTurn(true);

				updatePlantedCards(plantedToField, currentPlayer.getCardInBeanField()[plantedToField]);

			} else {

				boolean thirdFieldLocked = !gameFrame.getCurrentPlayerPanel().isThirdFieldEnabled();

				String message = "Can't plant this card.\n\n";
				if (thirdFieldLocked) {
					message += "Your first 2 fields are full with different bean types.\n\n" + "You can:\n"
							+ "• Harvest a field first, or\n" + "• Buy the third bean field for 3 coins";
				} else {
					message += "All fields are full with different bean types.\n"
							+ "You need to harvest a field first.";
				}

				JOptionPane.showMessageDialog(null, message, "Cannot Plant", JOptionPane.WARNING_MESSAGE);
			}
			break;

		case "Discard":
			SurfaceController.areaController.deckController.setTopDiscardCard(card);
			currentPlayer.getCardsInHand().remove(card);

			hasDiscardedThisTurn = true;

			if (SurfaceController.gameFrame != null) {
				SurfaceController.gameFrame.getOfferAreaPanel().getDiscardPile().setIcon(card.cardView());
			}
			break;

		default:
			break;
		}

		updatePlayersHand();
		updateCoinDisplay(currentPlayer);
	}

	public boolean isHasPlantedThisTurn() {
		return hasPlantedThisTurn;
	}

	public void setHasPlantedThisTurn(boolean val) {
		hasPlantedThisTurn = val;
	}

	public boolean hasDiscardedThisTurn() {
		return hasDiscardedThisTurn;
	}

	public void setHasDiscardedThisTurn(boolean val) {
		hasDiscardedThisTurn = val;
	}
}