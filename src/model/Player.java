package model;

import java.util.ArrayList;

/**
 * @author Mohan Dixit
 */

public class Player implements TurnInterface {

	private int coinTotal;
	private ArrayList<Card> cardsInHand = new ArrayList<Card>();
	public Card[] cardInBeanField = new Card[3];
	private int[] numberOfCardsInBeanField = new int[3];
	private boolean hasThirdField = false;

	public Player(ArrayList<Card> cardsInHand) {

		this.cardsInHand = cardsInHand;
		this.coinTotal = 0;
	}

	public int getCoinTotal() {
		return coinTotal;
	}

	public void setCoinTotal(int coinTotal) {
		this.coinTotal = coinTotal;
	}

	public ArrayList<Card> getCardsInHand() {
		return cardsInHand;
	}

	public void setCardsInHand(ArrayList<Card> cardsInHand) {
		this.cardsInHand = cardsInHand;
	}

	public Card[] getCardInBeanField() {
		return cardInBeanField;
	}

	public void setCardInBeanField(Card[] cardInBeanField) {
		this.cardInBeanField = cardInBeanField;
	}

	public int[] getNumberOfCardsInBeanField() {
		return numberOfCardsInBeanField;
	}

	public void setNumberOfCardsInBeanField(int[] numberOfCardsInBeanField) {
		this.numberOfCardsInBeanField = numberOfCardsInBeanField;
	}

	public boolean isHasThirdField() {
		return hasThirdField;
	}

	public boolean canPlant(Card card, int field) {

		if (field == 2 && !hasThirdField) {
			return false;
		}
		if (cardInBeanField[field] == null || cardInBeanField[field] == card) {

			return true;
		}
		return false;
	}

	public boolean canHarvest(int field) {
		if (numberOfCardsInBeanField[field] == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void discardCard(int cardIndex) {
		cardsInHand.remove(cardIndex);
	}

	@Override
	public void drawCard(Card card) {
		cardsInHand.add(card);
	}

	@Override
	public void plantBeans(int beanField) {

		if (canPlant(cardsInHand.get(0), beanField)) {
			cardInBeanField[beanField] = cardsInHand.get(0);
			numberOfCardsInBeanField[beanField]++;
			cardsInHand.remove(0);
		} else {
			System.out.println("Not possible to plant in that bean field.");
		}
	}

	@Override
	public void harvest(int beanField) {
		this.coinTotal += cardInBeanField[beanField].getSellingPrice()[numberOfCardsInBeanField[beanField]];
	}

	@Override
	public void buyField(int beanField) {
		if (this.coinTotal >= 3) {
			this.coinTotal -= 3;
			System.out.println("Succesful!");
			this.hasThirdField = true;

		} else {
			System.out.println("Not enough coins");
		}
	}

	public void addCardsToBeanField(int index, int numCards) {
		numberOfCardsInBeanField[index] += numCards;
	}

	public int countCardInHand(Card card) {
		int ans = 0;
		for (Card c : cardsInHand) {
			if (c.equals(card))
				ans++;
		}

		return ans;

	}

	public int countCardInFields(Card card) {
		int ans = 0;

		for (int i = 0; i < 3; i++) {

			if (cardInBeanField[i] == null)
				continue;

			if (card.equals(cardInBeanField[i])) {
				ans += numberOfCardsInBeanField[i];
			}
		}

		return ans;
	}

}