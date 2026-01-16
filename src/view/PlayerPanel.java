package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.*;

/**
 * @author Saran Rajagopal
 */

@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {

	private JButton button1, button2, button3, button7, button8;

	private JButton[] beanFields;
	private JLabel[] beanCounts;
	private JLabel coinTotal;

	private JScrollPane scrollPane;
	private JPanel scrollPanel;
	private JLabel startingCard;
	public JLabel currentTurn;

	private JLabel lockedFieldLabel;

	public PlayerPanel() {

		beanFields = new JButton[3];
		beanCounts = new JLabel[3];

		ImageIcon cardImg = new ImageIcon("src/images/Black-eyed.png");
		ImageIcon bf1 = new ImageIcon("src/images/Beanfield1.jpg");
		ImageIcon bf2 = new ImageIcon("src/images/Beanfield2.jpg");
		ImageIcon bf3 = new ImageIcon("src/images/Beanfield3.jpg");
		ImageIcon coin = new ImageIcon("src/images/goldcoin.png");

		Image scaledImage = coin.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(scaledImage);

		currentTurn = new JLabel("Current Turn");
		currentTurn.setFont(new Font("Verdana", Font.BOLD, 17));
		currentTurn.setBounds(503, 40, 190, 152);
		add(currentTurn);

		button1 = new JButton("card", cardImg);
		button2 = new JButton("card", cardImg);
		button3 = new JButton("card", cardImg);
		button7 = new JButton("card", cardImg);
		button8 = new JButton("card", cardImg);

		beanFields[0] = new JButton("", bf1);
		beanFields[1] = new JButton("", bf2);
		beanFields[2] = new JButton("", bf3);

		startingCard = new JLabel(new ImageIcon("src/images/Startingcard.jpg"));

		makeInvisible(button1);
		makeInvisible(button2);
		makeInvisible(button3);
		makeInvisible(button7);
		makeInvisible(button8);

		beanFields[0].setBounds(30, 10, 110, 152);
		beanFields[1].setBounds(142, 10, 110, 152);
		beanFields[2].setBounds(254, 10, 110, 152);

		beanFields[1].setBorderPainted(false);
		beanFields[1].setContentAreaFilled(false);
		beanFields[1].setFocusPainted(false);
		beanFields[1].setOpaque(false);

		beanFields[0].setBorderPainted(false);
		beanFields[0].setContentAreaFilled(false);
		beanFields[0].setFocusPainted(false);
		beanFields[0].setOpaque(false);

		add(beanFields[0]);
		add(beanFields[1]);
		add(beanFields[2]);

		beanCounts[0] = new JLabel("0");
		beanCounts[1] = new JLabel("0");
		beanCounts[2] = new JLabel("0");

		beanCounts[0].setBounds(75, 147, 50, 50);
		beanCounts[1].setBounds(190, 147, 50, 50);
		beanCounts[2].setBounds(280, 147, 50, 50);

		for (JLabel lbl : beanCounts) {
			lbl.setFont(new Font("Verdana", Font.BOLD, 20));
			add(lbl);
		}

		lockedFieldLabel = new JLabel("<html><center>🔒<br>Buy for<br>3 coins</center></html>");
		lockedFieldLabel.setBounds(254, 10, 110, 152);
		lockedFieldLabel.setFont(new Font("Verdana", Font.BOLD, 14));
		lockedFieldLabel.setForeground(Color.YELLOW);
		lockedFieldLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lockedFieldLabel.setVerticalAlignment(SwingConstants.CENTER);
		lockedFieldLabel.setOpaque(true);
		lockedFieldLabel.setBackground(new Color(101, 67, 33));
		lockedFieldLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
		add(lockedFieldLabel);

		disableThirdField();

		JLabel goldCounter = new JLabel(resizedIcon);
		goldCounter.setBounds(420, 0, 100, 100);
		add(goldCounter);

		coinTotal = new JLabel("0");
		coinTotal.setBounds(455, 60, 1000, 100);
		coinTotal.setFont(new Font("Verdana", Font.BOLD, 25));
		add(coinTotal);

		scrollPanel = new JPanel(null);
		scrollPanel.setPreferredSize(new java.awt.Dimension(600, 152));
		scrollPanel.setOpaque(false);

		button1.setBounds(0, 0, 110, 152);
		button2.setBounds(120, 0, 110, 152);
		button3.setBounds(240, 0, 110, 152);
		button7.setBounds(360, 0, 110, 152);
		button8.setBounds(480, 0, 110, 152);

		scrollPanel.add(button1);
		scrollPanel.add(button2);
		scrollPanel.add(button3);
		scrollPanel.add(button7);
		scrollPanel.add(button8);

		scrollPane = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollPane.setBounds(20, 185, 450, 169);

		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		add(scrollPane);

		startingCard.setBounds(490, 130, 150, 200);
		add(startingCard);

		setOpaque(false);
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(new Color(255, 0, 0, 200));
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);

		g2.dispose();
	}

	private void makeInvisible(JButton b) {
		b.setOpaque(false);
		b.setContentAreaFilled(false);
		b.setBorderPainted(false);
		b.setFocusPainted(false);
	}

	public void disableThirdField() {
		beanFields[2].setVisible(false);
		beanFields[2].setEnabled(false);
		beanCounts[2].setVisible(false);

		lockedFieldLabel.setVisible(true);
	}

	public void enableThirdField() {
		beanFields[2].setVisible(true);
		beanFields[2].setEnabled(true);
		beanCounts[2].setVisible(true);

		lockedFieldLabel.setVisible(false);

		ImageIcon bf3 = new ImageIcon("src/images/Beanfield3.jpg");
		beanFields[2].setIcon(bf3);
	}

	public boolean isThirdFieldEnabled() {
		return beanFields[2].isEnabled();
	}

	public void addNumCards(int beanFieldIndex, int numCards) {
		beanCounts[beanFieldIndex].setText(String.valueOf(numCards));
	}

	public void showCoins(int value) {
		coinTotal.setText(String.valueOf(value));
	}

	public JButton[] getBeanFields() {
		return beanFields;
	}

	public JLabel[] getBeanCounts() {
		return beanCounts;
	}

	public JButton getButton1() {
		return button1;
	}

	public JButton getButton2() {
		return button2;
	}

	public JButton getButton3() {
		return button3;
	}

	public JButton getButton7() {
		return button7;
	}

	public JButton getButton8() {
		return button8;
	}

	public JLabel getCoinTotal() {
		return coinTotal;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public JPanel getScrollPanel() {
		return scrollPanel;
	}

	public JLabel getStartingCard() {
		return startingCard;
	}
}