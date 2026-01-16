package controller;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import model.Card;
import model.Player;
import view.BohnanzaGameFrame;

/**
 * @author Mohan Dixit
 */

public class HardAIController {

	Player aiPlayer;
	private BohnanzaGameFrame gameFrame;

	public HardAIController(Player aiPlayer) {
		this.aiPlayer = aiPlayer;
		this.gameFrame = SurfaceController.gameFrame;
	}

	public void takeTurn() {

		gameFrame.currentStep.setText("Clear Offer Area");
		pause1s();
	    clearOfferAreaIfProfitable();
	    
	    gameFrame.currentStep.setText("Plant Cards");
	    pause1s();

	    plantOrDiscardHand();
	    
	    pause1s();
	    gameFrame.currentStep.setText("Plant Offer Area Cards");
	    plantOfferAreaCards();
	    
	    pause1s();
	    
	    gameFrame.currentStep.setText("Draw Two Cards");

	    drawTwoCards();
	    pause1s();
	}



	
	private void pause1s() {
	    JDialog dialog = new JDialog((Frame) null, true);
	    dialog.setUndecorated(true);
	    dialog.setSize(1, 1);
	    dialog.setLocationRelativeTo(null);

	    new javax.swing.Timer(1500, e -> dialog.dispose()).start();

	    dialog.setVisible(true);
	}


	private void clearOfferAreaIfProfitable() {
		for (int offerIndex = 0; offerIndex < 3; offerIndex++) {
			Card offerCard = SurfaceController.areaController.getOfferArea()[offerIndex];
			if (offerCard == null)
				continue;

			int bestField = findBestPlantField(offerCard,-0.5);
			if (bestField != -1 && expectedValue(offerCard, bestField) > 0) {
				plantOfferCardAtField(offerIndex, bestField);
			} else {
				discardOfferCard(offerIndex);
			}
		}
	}

	private void plantOrDiscardHand() {
		if (aiPlayer.getCardsInHand().isEmpty())
			return;
		ArrayList<Card> hand = aiPlayer.getCardsInHand();

		Card firstCard = hand.get(0);
		int firstField = findBestPlantField(firstCard, (int)-1e9);
		if (firstField != -1) {
			hand.remove(0);
			plantCardInField(firstCard, firstField);
			
		}
		pause1s();

		if (!hand.isEmpty()) {
			Card secondCard = hand.get(0);
			int secondField = findBestPlantField(secondCard,(int)-1e9);
			if (secondField != -1) {
				hand.remove(0);
				plantCardInField(secondCard, secondField);
				
			}
		}
		pause1s();

		if (!hand.isEmpty()) {
			Card discardCard = hand.get(0);
			double minEV = expectedValue(discardCard, 0);

			for (Card c : hand) {
				double lowestEV = Double.MAX_VALUE;
				for (int f = 0; f < 3; f++) {
					if (f == 2 && !gameFrame.getCurrentPlayerPanel().isThirdFieldEnabled())
						continue;
					double ev = expectedValue(c, f);
					if (ev < lowestEV)
						lowestEV = ev;
				}
				if (lowestEV < minEV) {
					minEV = lowestEV;
					discardCard = c;
				}
			}
			
			if(minEV<0) return;
			discardCard(discardCard);
			SurfaceController.playerController.updatePlayersHand();

		}
	}

	private void plantOfferAreaCards() {
		SurfaceController.areaController.drawOfferCards();
		
		
		
		Card[] offerArea = SurfaceController.areaController.getOfferArea();

		for (int i = 0; i < 3; i++) {
			pause1s();
			Card offerCard = offerArea[i];
			if (offerCard == null)
				continue;

			int bestField = findBestPlantField(offerCard,-0.5);

			double plantEV;
			if (bestField == -1) {
				plantEV = -1;
			} else {
				plantEV = expectedValue(offerCard, bestField);
			}

			double discardEV = -1.2;

			if (plantEV >= discardEV && bestField != -1) {
				plantOfferCardAtField(i, bestField);
			}
		}
	}

	private void drawTwoCards() {
		DeckController deck = SurfaceController.areaController.deckController;
		for (int i = 0; i < 2; i++) {

			if (deck.drawPile.empty())
				break;
			Card card = deck.getTopDeckCard();
			if (card != null)
				aiPlayer.getCardsInHand().add(card);
		}
	}

	private int findBestPlantField(Card card, double min) {
		double bestEV = min;
		int bestField = -1;
		for (int i = 0; i < 3; i++) {
			if (i == 2 && !gameFrame.getCurrentPlayerPanel().isThirdFieldEnabled()) {
				double ev = expectedValue(card, i) - 1.2;

				if (ev > bestEV && aiPlayer.getCoinTotal() >= 3) {
					bestEV = ev;
					bestField = i;
				}

				continue;
			}
			
			
			Card fieldCard = aiPlayer.getCardInBeanField()[i];
			if (fieldCard == null || fieldCard.equals(card)) {
				double ev = expectedValue(card, i);
				if (ev > bestEV) {
					bestEV = ev;
					bestField = i;
				}
			} else {
				if(!canHarvestField(i)) continue;
				double ev = expectedValue(card, i) - expectedHarvestEV(i);
				if (ev > bestEV) {
					bestEV = ev;
					bestField = i;
				}
			}
		}
		return bestField;
	}

	private double expectedValue(Card card, int fieldIndex) {

		Card[] fields = aiPlayer.getCardInBeanField();
		int[] counts = aiPlayer.getNumberOfCardsInBeanField();

		double currentFieldEV = 0;

		if (fields[fieldIndex] == null) {
			currentFieldEV = coinsForCards(card, aiPlayer.getNumberOfCardsInBeanField()[fieldIndex]);
		} else if (fields[fieldIndex].equals(card)) {
			int currentCount = counts[fieldIndex];

			double keepEV = coinsForCards(card, currentCount);

			double harvestEV = coinsForCards(card, currentCount)
					+ coinsForCards(card, aiPlayer.getNumberOfCardsInBeanField()[fieldIndex]);

			currentFieldEV = Math.max(keepEV, harvestEV);
		} else {
			int oldCount = counts[fieldIndex];
			double harvestEV = coinsForCards(fields[fieldIndex], oldCount)
					+ coinsForCards(card, aiPlayer.getNumberOfCardsInBeanField()[fieldIndex]);
			currentFieldEV = harvestEV;
		}

		int totalCopies = card.getTotalCopies();
		int inHand = aiPlayer.countCardInHand(card);
		int inFields = aiPlayer.countCardInFields(card);
		int seen = inHand + inFields;

		int remaining = totalCopies - seen;
		if (remaining < 0)
			remaining = 0;

		double futureEV = 0;
		for (int i = 1; i <= remaining; i++) {
			futureEV += coinsForCards(card, i) - coinsForCards(card, i - 1);
		}

		double handEV = 0;
		for (int i = 1; i < inHand; i++) {
			handEV += coinsForCards(card, i) - coinsForCards(card, i - 1);
		}

		double buyCost = 0;
		if (fieldIndex == 2 && !aiPlayer.isHasThirdField()) {
			buyCost = 3;
		}

		return currentFieldEV + futureEV + handEV - buyCost;
	}

	private int coinsForCards(Card card, int numCards) {
		int[] priceTable = card.getSellingPrice();
		int coinsEarned = 0;
		for (int i = 0; i < priceTable.length; i++) {
			if (numCards >= priceTable[i])
				coinsEarned = i + 1;
		}
		return coinsEarned;
	}

	private double expectedHarvestEV(int fieldIndex) {
		Card[] fields = aiPlayer.getCardInBeanField();
		int[] counts = aiPlayer.getNumberOfCardsInBeanField();
		Card card = fields[fieldIndex];
		if (card == null || counts[fieldIndex] == 0)
			return 0;
		int currentCount = counts[fieldIndex];

		int[] priceTable = card.getSellingPrice();
		int coinsNow = 0;
		for (int i = 0; i < priceTable.length; i++) {
			if (currentCount >= priceTable[i])
				coinsNow = i + 1;
		}

		if (coinsNow == priceTable[priceTable.length - 1]) {
			return 1e9;
		}
		int total = card.getTotalCopies();
		int inHand = aiPlayer.countCardInHand(card);
		int inFields = aiPlayer.countCardInFields(card);

		Player opponent = SurfaceController.playerController.getPlayer1();

		int opponentHas = opponent.countCardInFields(card);
		int remaining = total - (inHand + inFields + opponentHas);
		double expectedLater = coinsNow;
		for (int extra = 1; extra <= remaining; extra++) {
			int newCount = currentCount + extra;
			int newCoins = 0;
			for (int i = 0; i < priceTable.length; i++) {
				if (newCount >= priceTable[i])
					newCoins = i + 1;
			}
			int gain = newCoins - coinsNow;
			double prob = (double) extra / remaining;
			expectedLater += gain * prob;
		}
		double riskFactor = 0.25;
		return coinsNow + (expectedLater - coinsNow) * riskFactor;
	}

	private void plantCardInField(Card card, int field) {
		Card[] beanFields = aiPlayer.getCardInBeanField();

		if (beanFields[field] != null && !beanFields[field].equals(card)) {

			if (!canHarvestField(field))
				return;
			SurfaceController.playerController.sellCards(field);
			beanFields[field] = null;
			aiPlayer.getNumberOfCardsInBeanField()[field] = 0;
		}

		if (field == 2 && !aiPlayer.isHasThirdField()) {
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

		if (beanFields[field] == null) {
			beanFields[field] = card;
			aiPlayer.getNumberOfCardsInBeanField()[field] = 1;
		} else {
			aiPlayer.addCardsToBeanField(field, 1);
		}

		SurfaceController.playerController.updatePlantedCards(field, beanFields[field]);
		SurfaceController.playerController.updatePlayersHand();

	}

	private void plantOfferCardAtField(int offerIndex, int field) {
		Card card = SurfaceController.areaController.getOfferArea()[offerIndex];
		int offerCount = SurfaceController.areaController.numberOfCardsInOfferArea[offerIndex];

		plantCardInField(card, field);

		if (aiPlayer.getCardInBeanField()[field] != null && aiPlayer.getCardInBeanField()[field].equals(card)
				&& offerCount > 1) {
			aiPlayer.addCardsToBeanField(field, offerCount - 1);
		}

		SurfaceController.areaController.offerArea[offerIndex] = null;
		SurfaceController.areaController.numberOfCardsInOfferArea[offerIndex] = 0;

		SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[offerIndex]
				.setIcon(new ImageIcon("src/images/Bacsk.png"));
		SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[offerIndex].setText("0");

		SurfaceController.playerController.updatePlantedCards(field, aiPlayer.getCardInBeanField()[field]);
	}

	private void discardCard(Card card) {
		SurfaceController.areaController.deckController.setTopDiscardCard(card);
		aiPlayer.getCardsInHand().remove(card);
		gameFrame.getOfferAreaPanel().getDiscardPile().setIcon(card.cardView());
	}

	private void discardOfferCard(int offerIndex) {
		Card card = SurfaceController.areaController.getOfferArea()[offerIndex];
		for (int i = 0; i < SurfaceController.areaController.numberOfCardsInOfferArea[offerIndex]; i++) {
			SurfaceController.areaController.deckController.setTopDiscardCard(card);
		}
		SurfaceController.areaController.getOfferArea()[offerIndex] = null;
		SurfaceController.areaController.numberOfCardsInOfferArea[offerIndex] = 0;
		gameFrame.getOfferAreaPanel().getOfferAreas()[offerIndex].setIcon(new ImageIcon("blank.png"));
		gameFrame.getOfferAreaPanel().getOfferAreaCounters()[offerIndex].setText("0");
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
}