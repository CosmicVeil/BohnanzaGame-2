package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import javax.swing.ImageIcon;
import model.Card;

/**
 * @author Saran Rajagopal
 */

public class DeckController {

	public Stack<Card> drawPile = new Stack<Card>();
	public Stack<Card> discardPile = new Stack<Card>();
	private ArrayList<ArrayList<Card>> offerArea = new ArrayList<>();

	public DeckController(Stack<Card> drawPile, Stack<Card> discardPile, ArrayList<ArrayList<Card>> offerArea) {
		super();
		this.drawPile = drawPile;
		this.discardPile = discardPile;
		this.offerArea = offerArea;
	}

	public DeckController() {
		initializeDeck();
	}

	public Stack<Card> getDrawPile() {
		return drawPile;
	}

	public void setDrawPile(Stack<Card> drawPile) {
		this.drawPile = drawPile;
	}

	public Stack<Card> getDiscardPile() {
		return discardPile;
	}

	public void setDiscardPile(Stack<Card> discardPile) {
		this.discardPile = discardPile;
	}

	public ArrayList<ArrayList<Card>> getOfferArea() {
		return offerArea;
	}

	public void setOfferArea(ArrayList<ArrayList<Card>> offerArea) {
		this.offerArea = offerArea;
	}

	public Card getTopDeckCard() {
		SurfaceController.gameFrame.getOfferAreaPanel().numCardsInDeck
				.setText(String.valueOf(Math.max(drawPile.size() - 1, 0)));
		return drawPile.pop();
	}

	public Card getTopDiscardCard() {
		Card temp = discardPile.pop();

		SurfaceController.gameFrame.getOfferAreaPanel().getDiscardPile()
				.setIcon(new ImageIcon(discardPile.getLast().getLink()));
		return temp;
	}

	public void setTopDiscardCard(Card card) {
		discardPile.add(card);
	}

	public void initializeDeck() {
		// Red Bean 8 copies
		for (int i = 0; i < 8; i++) {
			drawPile.add(new Card("Red", new int[] { 2, 3, 4, 5 }, 8));
		}
		// Black-eyed Bean 10 copies
		for (int i = 0; i < 10; i++) {
			drawPile.add(new Card("Black-eyed", new int[] { 2, 4, 5, 6 }, 10));
		}
		// Soy Bean 12 copies
		for (int i = 0; i < 12; i++) {
			drawPile.add(new Card("Soy", new int[] { 2, 4, 6, 7 }, 12));
		}
		// Green Bean 14 copies
		for (int i = 0; i < 14; i++) {
			drawPile.add(new Card("Green", new int[] { 3, 5, 6, 7 }, 14));
		}
		// Stink Bean 16 copies
		for (int i = 0; i < 16; i++) {
			drawPile.add(new Card("Stink", new int[] { 3, 5, 7, 8 }, 16));
		}
		// Chili Bean 18 copies
		for (int i = 0; i < 18; i++) {
			drawPile.add(new Card("Chili", new int[] { 3, 6, 8, 9 }, 18));
		}
		// Blue Bean 20 copies
		for (int i = 0; i < 20; i++) {
			drawPile.add(new Card("Blue", new int[] { 4, 6, 8, 10 }, 20));
		}

		shuffleDeck();
	}

	public void shuffleDeck() {
		Collections.shuffle(drawPile);

	}

	public boolean isEmpty() {
		return (drawPile.size() == 0);
	}

	public void clearOfferAreaPile() {
		getOfferArea().clear();
	}

}