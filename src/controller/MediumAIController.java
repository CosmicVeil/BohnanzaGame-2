package controller;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import model.Card;
import model.Player;
import view.BohnanzaGameFrame;

/**
 * @author Vishal Parvannan - Enhanced Medium AI
 */

public class MediumAIController {

	Player aiPlayer;
	private BohnanzaGameFrame gameFrame;
	private Random rand = new Random();

	public MediumAIController(Player aiPlayer) {
		this.aiPlayer = aiPlayer;
		this.gameFrame = SurfaceController.gameFrame;
	}

	public void takeTurn() {
		gameFrame = SurfaceController.gameFrame;

		gameFrame.currentStep.setText("Clear Offer Area");
		pause1s();
		handleOfferAreaCards();

		gameFrame.currentStep.setText("Plant Hand Cards");
		pause1s();
		plantHandCards();

		gameFrame.currentStep.setText("Draw Offer Area Cards");
		pause1s();
		drawOfferAreaCards();

		gameFrame.currentStep.setText("Draw Two Cards");
		pause1s();
		drawTwoCards();

		SurfaceController.playerController.updateCoinDisplay(aiPlayer);
	}

	private void pause1s() {
		JDialog dialog = new JDialog((Frame) null, true);
		dialog.setUndecorated(true);
		dialog.setSize(1, 1);
		dialog.setLocationRelativeTo(null);

		new javax.swing.Timer(1500, e -> dialog.dispose()).start();

		dialog.setVisible(true);
	}

	private void handleOfferAreaCards() {
		Card[] offers = SurfaceController.areaController.getOfferArea();
		int[] offerCounts = SurfaceController.areaController.numberOfCardsInOfferArea;

		for (int i = 0; i < offers.length; i++) {
			Card offer = offers[i];
			if (offer == null)
				continue;

			int field = findBestFieldToPlant(offer, offerCounts[i]);

			if (field == -1) {
				if (offerCounts[i] < 2 || !isValuableCard(offer)) {
					discardOfferCard(i);
				} else {
					field = selectFieldToHarvest(offer);
					if (field != -1 && canHarvestField(field)) {
						harvestField(field);
						pause1s();
						plantOfferCard(offer, field, i);
					} else {
						discardOfferCard(i);
					}
				}
			} else {
				plantOfferCard(offer, field, i);
			}

			pause1s();
		}

		SurfaceController.playerController.updateCoinDisplay(aiPlayer);
	}

	private void plantHandCards() {
		ArrayList<Card> hand = aiPlayer.getCardsInHand();

		if (!hand.isEmpty()) {
			Card firstCard = hand.get(0);
			int field = findBestFieldToPlant(firstCard, 1);

			if (field == -1) {
				field = forceHarvestAndGetField(firstCard);
			}

			if (field != -1) {
				plantHandCard(firstCard, field);
				hand.remove(firstCard);
				SurfaceController.playerController.updatePlayersHand();
				pause1s();
			}
		}

		if (!hand.isEmpty() && rand.nextInt(10) < 7) {
			Card secondCard = hand.get(0);
			int field = findBestFieldToPlant(secondCard, 1);

			if (field != -1) {
				plantHandCard(secondCard, field);
				hand.remove(secondCard);
				SurfaceController.playerController.updatePlayersHand();
				pause1s();
			} else if (isValuableCard(secondCard) && rand.nextBoolean()) {
				field = selectFieldToHarvest(secondCard);
				if (field != -1 && canHarvestField(field)) {
					harvestField(field);
					pause1s();
					plantHandCard(secondCard, field);
					hand.remove(secondCard);
					SurfaceController.playerController.updatePlayersHand();
					pause1s();
				}
			}
		}

		if (!hand.isEmpty() && rand.nextInt(5) < 2) {
			Card discardCard = hand.get(0);
			int minValue = getCardValue(discardCard);
			
			for (Card c : hand) {
				int value = getCardValue(c);
				if (value < minValue) {
					minValue = value;
					discardCard = c;
				}
			}
			
			discardHandCard(discardCard);
			hand.remove(discardCard);
			SurfaceController.playerController.updatePlayersHand();
			pause1s();
		}

		SurfaceController.playerController.updateCoinDisplay(aiPlayer);
	}

	private int findBestFieldToPlant(Card card, int cardCount) {
		Card[] fields = aiPlayer.getCardInBeanField();
		int[] counts = aiPlayer.getNumberOfCardsInBeanField();
		int fieldsToCheck = hasThirdField() ? 3 : 2;

		for (int i = 0; i < fieldsToCheck; i++) {
			if (fields[i] != null && fields[i].equals(card)) {
				return i;
			}
		}

		for (int i = 0; i < fieldsToCheck; i++) {
			if (fields[i] == null) {
				return i;
			}
		}

		if (!hasThirdField() && aiPlayer.getCoinTotal() >= 3 && isValuableCard(card)) {
			return 2;
		}

		int bestField = -1;
		int lowestScore = Integer.MAX_VALUE;

		for (int i = 0; i < fieldsToCheck; i++) {
			if (fields[i] != null && counts[i] <= 2 && canHarvestField(i)) {
				int currentValue = getCardValue(fields[i]) * counts[i];
				int newValue = getCardValue(card) * cardCount;

				if (newValue > currentValue * 1.5 && currentValue < lowestScore) {
					lowestScore = currentValue;
					bestField = i;
				}
			}
		}

		return bestField;
	}

	private int forceHarvestAndGetField(Card card) {
		int field = selectFieldToHarvest(card);
		if (field != -1 && canHarvestField(field)) {
			harvestField(field);
			pause1s();
		}
		return field;
	}

	private int selectFieldToHarvest(Card newCard) {
		Card[] fields = aiPlayer.getCardInBeanField();
		int[] counts = aiPlayer.getNumberOfCardsInBeanField();
		int fieldsToCheck = hasThirdField() ? 3 : 2;

		int worstField = -1;
		int worstScore = Integer.MAX_VALUE;

		for (int i = 0; i < fieldsToCheck; i++) {
			if (fields[i] != null && canHarvestField(i)) {
				int fieldScore = getCardValue(fields[i]) * counts[i] * 10 + counts[i];
				
				if (fieldScore < worstScore) {
					worstScore = fieldScore;
					worstField = i;
				}
			}
		}

		return worstField;
	}

	private void drawOfferAreaCards() {
		SurfaceController.areaController.drawOfferCards();
		pause1s();
	}

	private void drawTwoCards() {
		DeckController deck = SurfaceController.areaController.deckController;
		for (int i = 0; i < 2; i++) {
			if (deck.drawPile.empty())
				break;
			Card card = deck.getTopDeckCard();
			if (card != null) {
				aiPlayer.getCardsInHand().add(card);
			}
		}
		SurfaceController.playerController.updatePlayersHand();
		SurfaceController.playerController.updateCoinDisplay(aiPlayer);
	}

	private boolean hasThirdField() {
		if (aiPlayer == SurfaceController.playerController.getPlayer1()) {
			return gameFrame.getPlayerPanel1().isThirdFieldEnabled();
		}
		return gameFrame.getPlayerPanel2().isThirdFieldEnabled();
	}

	private boolean canHarvestField(int field) {
		if (aiPlayer.getNumberOfCardsInBeanField()[field] > 1)
			return true;

		for (int i = 0; i < 3; i++) {
			if (i == field)
				continue;
			if (aiPlayer.isHasThirdField() || i < 2) {
				if (aiPlayer.getCardInBeanField()[i] != null && aiPlayer.getNumberOfCardsInBeanField()[i] > 1) {
					return false;
				}
			}
		}
		return true;
	}

	private void harvestField(int fieldIndex) {
		Card bean = aiPlayer.getCardInBeanField()[fieldIndex];
		int numCards = aiPlayer.getNumberOfCardsInBeanField()[fieldIndex];

		if (bean != null) {
			for (int i = 0; i < numCards; i++) {
				SurfaceController.areaController.deckController.setTopDiscardCard(bean);
			}
			SurfaceController.gameFrame.getOfferAreaPanel().getDiscardPile().setIcon(bean.cardView());
		}

		SurfaceController.playerController.sellCards(fieldIndex);
		SurfaceController.playerController.updatePlantedCards(fieldIndex, null);
	}

	private void plantHandCard(Card card, int fieldIndex) {
		Card[] fields = aiPlayer.getCardInBeanField();

		if (fieldIndex == -1)
			return;

		if (fields[fieldIndex] != null && !fields[fieldIndex].equals(card)) {
			if (!canHarvestField(fieldIndex))
				return;
			harvestField(fieldIndex);
			pause1s();
		}

		if (fieldIndex == 2 && !hasThirdField()) {
			if (aiPlayer.getCoinTotal() >= 3) {
				aiPlayer.buyField(2);
				if (aiPlayer == SurfaceController.playerController.getPlayer1())
					gameFrame.getPlayerPanel1().enableThirdField();
				else
					gameFrame.getPlayerPanel2().enableThirdField();

				SurfaceController.playerController.updateCoinDisplay(aiPlayer);
			} else {
				return;
			}
		}

		if (fields[fieldIndex] == null) {
			fields[fieldIndex] = card;
			aiPlayer.getNumberOfCardsInBeanField()[fieldIndex] = 1;
		} else {
			aiPlayer.addCardsToBeanField(fieldIndex, 1);
		}

		SurfaceController.playerController.updatePlantedCards(fieldIndex, fields[fieldIndex]);
	}

	private void plantOfferCard(Card card, int fieldIndex, int offerIndex) {
		Card[] fields = aiPlayer.getCardInBeanField();
		int[] offerCounts = SurfaceController.areaController.numberOfCardsInOfferArea;

		if (fields[fieldIndex] != null && !fields[fieldIndex].equals(card)) {
			if (!canHarvestField(fieldIndex))
				return;
			harvestField(fieldIndex);
		}

		if (fieldIndex == 2 && !hasThirdField()) {
			if (aiPlayer.getCoinTotal() >= 3) {
				aiPlayer.buyField(2);
				if (aiPlayer == SurfaceController.playerController.getPlayer1())
					gameFrame.getPlayerPanel1().enableThirdField();
				else
					gameFrame.getPlayerPanel2().enableThirdField();

				SurfaceController.playerController.updateCoinDisplay(aiPlayer);
			} else {
				return;
			}
		}

		if (fields[fieldIndex] == null) {
			fields[fieldIndex] = card;
			aiPlayer.getNumberOfCardsInBeanField()[fieldIndex] = offerCounts[offerIndex];
		} else {
			aiPlayer.addCardsToBeanField(fieldIndex, offerCounts[offerIndex]);
		}

		SurfaceController.areaController.getOfferArea()[offerIndex] = null;
		SurfaceController.areaController.numberOfCardsInOfferArea[offerIndex] = 0;

		updateOfferAreaGUI(offerIndex);
		SurfaceController.playerController.updatePlantedCards(fieldIndex, fields[fieldIndex]);
	}

	private void updateOfferAreaGUI(int i) {
		SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[i]
				.setIcon(new ImageIcon("src/images/Bacsk.png"));
		SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[i].setText("0");
	}

	private void discardHandCard(Card card) {
		SurfaceController.areaController.deckController.setTopDiscardCard(card);
		gameFrame.getOfferAreaPanel().getDiscardPile().setIcon(card.cardView());
	}

	private void discardOfferCard(int index) {
		Card card = SurfaceController.areaController.getOfferArea()[index];
		int count = SurfaceController.areaController.numberOfCardsInOfferArea[index];

		if (card != null) {
			for (int i = 0; i < count; i++) {
				SurfaceController.areaController.deckController.setTopDiscardCard(card);
			}

			SurfaceController.gameFrame.getOfferAreaPanel().getDiscardPile().setIcon(card.cardView());
		}

		SurfaceController.areaController.getOfferArea()[index] = null;
		SurfaceController.areaController.numberOfCardsInOfferArea[index] = 0;

		updateOfferAreaGUI(index);
	}

	private int getCardValue(Card card) {
		int[] priceTable = card.getSellingPrice();
		if (priceTable.length > 0) {
			return 10 - priceTable[0];
		}
		return 5;
	}

	private boolean isValuableCard(Card card) {
		int[] priceTable = card.getSellingPrice();
		return priceTable.length > 0 && priceTable[0] <= 3;
	}
}