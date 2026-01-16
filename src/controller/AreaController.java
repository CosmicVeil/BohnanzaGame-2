package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import model.Card;
import model.Player;
import view.OfferAreaPanel;
import view.PlayerPanel;

/**
 * @author Mohan Dixit
 */

public class AreaController implements ActionListener {

	Card[] offerArea = new Card[3];
	int[] numberOfCardsInOfferArea = new int[3];

	DeckController deckController = new DeckController();

	public AreaController(Card[] offerArea, int[] numberOfCardsInOfferAre) {
		super();
		this.offerArea = offerArea;
		this.numberOfCardsInOfferArea = numberOfCardsInOfferAre;

		for (int i = 0; i < 3; i++)
			SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[i].addActionListener(this);
	}

	public AreaController() {
		for (int i = 0; i < 3; i++)
			SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[i].addActionListener(this);
	}

	public Card[] getOfferArea() {
		return offerArea;
	}

	public void setOfferArea(Card[] offerArea) {
		this.offerArea = offerArea;
	}

	public void drawOfferCards() {
		OfferAreaPanel panel = SurfaceController.gameFrame.getOfferAreaPanel();

		int totalCards = 0;
		int i = 0;
		
		outer:
		while (totalCards < 3) {
			totalCards++;

			offerArea[i] = deckController.getTopDeckCard();
			numberOfCardsInOfferArea[i] = 1;

			while (deckController.discardPile.size() > 0 && offerArea[i].equals(deckController.discardPile.getLast())) {
				deckController.discardPile.pop();
				numberOfCardsInOfferArea[i]++;
			}

			for (int j = 0; j < i; j++) {
				if (offerArea[i].equals(offerArea[j])) {
					panel.getOfferAreaCounters()[j]
							.setText("" + (numberOfCardsInOfferArea[j] + numberOfCardsInOfferArea[i]));
					numberOfCardsInOfferArea[j] += numberOfCardsInOfferArea[i];
					numberOfCardsInOfferArea[i] = 0;
					offerArea[i] = null;
					continue outer;
				}
			}
			panel.getOfferAreas()[i].setIcon(offerArea[i].cardView());
			panel.getOfferAreaCounters()[i].setText("" + numberOfCardsInOfferArea[i]);

			if (deckController.discardPile.size() > 0) {
				panel.getDiscardPile().setIcon(deckController.discardPile.getLast().cardView());
			} else {
				panel.getDiscardPile().setIcon(new ImageIcon("blank.png"));
			}
			i++;
		}
		panel.revalidate();
		panel.repaint();
	}

	public void addCardtoOfferArea(Card card, int pileIndex) {

		offerArea[pileIndex] = card;
	}

	public void addToDiscardPile(int pileIndex) {

		for (int i = 0; i < 3; i++) {
			while (numberOfCardsInOfferArea[i] > 0) {
				deckController.setTopDiscardCard(offerArea[i]);
				numberOfCardsInOfferArea[i]--;
			}
		}

	}

	public boolean isAreaEmpty(int pileIndex) {
		return numberOfCardsInOfferArea[pileIndex] == 0;
	}

	public void clearOfferAreaPile() {
		offerArea = new Card[0];
		numberOfCardsInOfferArea = new int[] { 0, 0, 0 };
	}

	public boolean isMatchingPile(Card card, int index) {

		Card topCard = offerArea[index];

		return card.equals(topCard);
	}

	private boolean isFieldAvailable(Player player, int fieldIndex) {
		if (fieldIndex < 0 || fieldIndex > 2)
			return false;
		if (fieldIndex < 2)
			return true;

		PlayerPanel panel;
		if (player == SurfaceController.playerController.player1) {
			panel = SurfaceController.gameFrame.getPlayerPanel1();
		} else {
			panel = SurfaceController.gameFrame.getPlayerPanel2();
		}
		return panel.isThirdFieldEnabled();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (SurfaceController.getGamestates()[SurfaceController.getGameState()].equals("DrawOfferAreaCards")) {

			int index = 0;
			for (int i = 0; i < 3; i++) {
				if (e.getSource() == SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[i]) {
					index = i;
					break;
				}
			}

			if (offerArea[index] == null) {
				return;
			}

			int result = JOptionPane.showConfirmDialog(null, "Plant?", "Confirm", JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				Player currPlayer = SurfaceController.playerController.getCurrentPlayer();

				Card card = offerArea[index];
				PlayerPanel currPlayerPanel;

				if (currPlayer == SurfaceController.playerController.player1) {
					currPlayerPanel = SurfaceController.gameFrame.getPlayerPanel1();
				} else {
					currPlayerPanel = SurfaceController.gameFrame.getPlayerPanel2();
				}

				for (int i = 0; i < 3; i++) {

					if (!isFieldAvailable(currPlayer, i))
						continue;

					Card c = currPlayer.getCardInBeanField()[i];

					if (c != null && c.equals(card)) {
						currPlayer.addCardsToBeanField(i, numberOfCardsInOfferArea[index]);

						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[index]
								.setIcon(new ImageIcon("src/images/Bacsk.png"));
						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[index].setText("0");

						currPlayerPanel.addNumCards(i, currPlayer.getNumberOfCardsInBeanField()[i]);
						offerArea[index] = null;
						numberOfCardsInOfferArea[index] = 0;
						return;
					}
				}

				for (int i = 0; i < 3; i++) {

					if (!isFieldAvailable(currPlayer, i))
						continue;

					Card c = currPlayer.getCardInBeanField()[i];

					if (c == null) {
						currPlayer.addCardsToBeanField(i, numberOfCardsInOfferArea[index]);
						currPlayer.cardInBeanField[i] = card;

						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[index]
								.setIcon(new ImageIcon("src/images/Bacsk.png"));
						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[index].setText("0");

						currPlayerPanel.addNumCards(i, currPlayer.getNumberOfCardsInBeanField()[i]);
						currPlayerPanel.getBeanFields()[i].setIcon(card.cardView());
						offerArea[index] = null;
						numberOfCardsInOfferArea[index] = 0;
						return;
					}
				}

				JOptionPane.showMessageDialog(null, "Can't plant into any existing field.");
			}

			else if (result == JOptionPane.NO_OPTION && SurfaceController.getGameState() == 1) {

				Card card = offerArea[index];
				SurfaceController.areaController.deckController.setTopDiscardCard(card);

				SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[index]
						.setIcon(new ImageIcon("src/images/Basck.png"));
				SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[index].setText("0");
				SurfaceController.areaController.deckController.setTopDiscardCard(card);

				offerArea[index] = null;
				numberOfCardsInOfferArea[index] = 0;
			}

		} else if (SurfaceController.getGamestates()[SurfaceController.getGameState()].equals("ClearOfferArea")) {

			int index = -1;
			for (int i = 0; i < 3; i++) {
				if (e.getSource() == SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[i]) {
					index = i;
					break;
				}
			}

			if (offerArea[index] == null) {
				return;
			}
			if (index == -1)
				return;

			int choice = JOptionPane.showOptionDialog(null, "Plant or Discard?", "Choose", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, new String[] { "Plant", "Discard" }, null);

			Player currPlayer = SurfaceController.playerController.getCurrentPlayer();
			Card card = offerArea[index];

			if (choice == 0) {
				PlayerPanel currPlayerPanel;

				if (currPlayer == SurfaceController.playerController.player1) {
					currPlayerPanel = SurfaceController.gameFrame.getPlayerPanel1();
				} else {
					currPlayerPanel = SurfaceController.gameFrame.getPlayerPanel2();
				}

				for (int i = 0; i < 3; i++) {

					if (!isFieldAvailable(currPlayer, i))
						continue;

					Card c = currPlayer.getCardInBeanField()[i];

					if (c != null && c.equals(card)) {
						currPlayer.addCardsToBeanField(i, numberOfCardsInOfferArea[index]);

						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[index]
								.setIcon(new ImageIcon("src/images/Bacsk.png"));
						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[index].setText("0");

						currPlayerPanel.addNumCards(i, currPlayer.getNumberOfCardsInBeanField()[i]);
						offerArea[index] = null;
						numberOfCardsInOfferArea[index] = 0;
						return;
					}
				}

				for (int i = 0; i < 3; i++) {

					if (!isFieldAvailable(currPlayer, i))
						continue;

					Card c = currPlayer.getCardInBeanField()[i];

					if (c == null) {
						currPlayer.addCardsToBeanField(i, numberOfCardsInOfferArea[index]);
						currPlayer.cardInBeanField[i] = card;

						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[index]
								.setIcon(new ImageIcon("src/images/Bacsk.png"));
						SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[index].setText("0");

						currPlayerPanel.addNumCards(i, currPlayer.getNumberOfCardsInBeanField()[i]);
						currPlayerPanel.getBeanFields()[i].setIcon(card.cardView());
						offerArea[index] = null;
						numberOfCardsInOfferArea[index] = 0;
						return;
					}
				}

				PlayerPanel ownerPanel = currPlayerPanel;
				boolean thirdFieldLocked = !ownerPanel.isThirdFieldEnabled();

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

			if (choice == 1) {

				SurfaceController.gameFrame.getOfferAreaPanel().getDiscardPile().setIcon(new ImageIcon(card.getLink()));

				for (int i = 0; i < numberOfCardsInOfferArea[index]; i++) {
					deckController.setTopDiscardCard(card);
				}

				offerArea[index] = null;
				numberOfCardsInOfferArea[index] = 0;

				SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreas()[index]
						.setIcon(new ImageIcon("src/images/Bacsk.png"));
				SurfaceController.gameFrame.getOfferAreaPanel().getOfferAreaCounters()[index].setText("0");

				return;
			}
		}
	}

	public boolean isDeckEmpty() {
		return deckController.isEmpty();
	}

}