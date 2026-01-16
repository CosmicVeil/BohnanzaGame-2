package view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;

/**
 * @author Mohan Dixit
 */

@SuppressWarnings("serial")
public class OfferAreaPanel extends JPanel {

	private JLabel deckLabel;
	private JButton[] offerAreas;
	private JLabel[] offerAreaCounters;
	private JLabel[] offerAreaLabels;
	private JLabel discardPile;
	private JLabel discardPileLabel;

	public JLabel numCardsInDeck;
	public JLabel numCardsInDiscard;

	public OfferAreaPanel() {

		setLayout(null);

		offerAreas = new JButton[3];
		offerAreaCounters = new JLabel[3];
		offerAreaLabels = new JLabel[3];

		deckLabel = new JLabel(new ImageIcon("src/images/Back.png"));
		deckLabel.setBounds(0, 0, 100, 300);
		add(deckLabel);

		numCardsInDeck = new JLabel("98");
		numCardsInDeck.setBounds(28, 25, 50, 50);
		numCardsInDeck.setFont(new Font("Roboto", Font.ITALIC, 40));
		numCardsInDeck.setForeground(Color.black);
		add(numCardsInDeck);

		discardPile = new JLabel(new ImageIcon("src/images/Bacsk.png"));
		discardPile.setOpaque(false);
		discardPile.setBorder(BorderFactory.createDashedBorder(Color.WHITE, 10, 5));
		discardPile.setBounds(150, 73, 100, 152);
		add(discardPile);

		discardPileLabel = new JLabel("Discard Pile");
		discardPileLabel.setHorizontalAlignment(SwingConstants.CENTER);
		discardPileLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
		discardPileLabel.setForeground(Color.WHITE);
		discardPileLabel.setBounds(150, 140, 100, 20);
		add(discardPileLabel);

		addOfferAreas();
	}

	public JLabel getDeckLabel() {
		return deckLabel;
	}

	public void setDeckLabel(JLabel deckLabel) {
		this.deckLabel = deckLabel;
	}

	public JButton[] getOfferAreas() {
		return offerAreas;
	}

	public void setOfferAreas(JButton[] offerAreas) {
		this.offerAreas = offerAreas;
	}

	public JLabel[] getOfferAreaCounters() {
		return offerAreaCounters;
	}

	public void setOfferAreaCounters(JLabel[] offerAreaCounters) {
		this.offerAreaCounters = offerAreaCounters;
	}

	public JLabel getDiscardPile() {
		return discardPile;
	}

	public void setDiscardPile(JLabel discardPile) {
		this.discardPile = discardPile;
	}

	private void addOfferAreas() {

		for (int i = 0; i < 3; i++) {

			offerAreas[i] = new JButton(new ImageIcon("src/images/Basck.png"));
			offerAreas[i].setOpaque(false);
			offerAreas[i].setContentAreaFilled(false);
			offerAreas[i].setBorderPainted(true);
			offerAreas[i].setFocusPainted(false);
			offerAreas[i].setBorder(BorderFactory.createDashedBorder(Color.WHITE, 10, 5));
			offerAreas[i].setBounds(330 + i * 150, 78, 100, 152);

			offerAreaLabels[i] = new JLabel("Offer Area #" + (i + 1));
			offerAreaLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			offerAreaLabels[i].setFont(new Font("SansSerif", Font.ITALIC, 15));
			offerAreaLabels[i].setForeground(Color.WHITE);
			offerAreaLabels[i].setBounds(330 + i * 150, 145, 100, 20);


			offerAreaCounters[i] = new JLabel("0");
			offerAreaCounters[i].setBounds(365 + i * 150, -100, 100, 300);
			offerAreaCounters[i].setFont(new Font("Roboto", Font.ITALIC, 40));
			offerAreaCounters[i].setForeground(Color.black);

			add(offerAreas[i]);
			add(offerAreaLabels[i]);
			add(offerAreaCounters[i]);
		}
	}
}