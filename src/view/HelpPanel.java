package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * @author Saran Rajagopal
 */

@SuppressWarnings("serial")
public class HelpPanel extends JPanel {

	public ImageIcon panelBackground = new ImageIcon("src/images/red.jpg");
	Image scaledImage = panelBackground.getImage().getScaledInstance(700, 700, Image.SCALE_SMOOTH);

	public ImageIcon icon = new ImageIcon("src/images/peach.png");

	public JButton Helpbutton = new JButton("Help", icon);
	public JButton button2 = new JButton("Next Step", icon);
	public JButton button3 = new JButton("Menu", icon);
	public JButton button4 = new JButton("Buy Field", icon);

	public HelpPanel() {

		setLayout(null);

		Helpbutton.setFont(new Font("Arial", Font.BOLD, 18));
		Helpbutton.setHorizontalTextPosition(SwingConstants.CENTER);
		Helpbutton.setVerticalTextPosition(SwingConstants.CENTER);

		button2.setFont(new Font("Arial", Font.BOLD, 18));
		button2.setHorizontalTextPosition(SwingConstants.CENTER);
		button2.setVerticalTextPosition(SwingConstants.CENTER);

		button3.setFont(new Font("Arial", Font.BOLD, 18));
		button3.setHorizontalTextPosition(SwingConstants.CENTER);
		button3.setVerticalTextPosition(SwingConstants.CENTER);

		button4.setFont(new Font("Arial", Font.BOLD, 18));
		button4.setHorizontalTextPosition(SwingConstants.CENTER);
		button4.setVerticalTextPosition(SwingConstants.CENTER);

		Helpbutton.setBounds(50, 50, 100, 55);
		button2.setBounds(200, 50, 125, 55);
		button3.setBounds(375, 50, 100, 55);
		button4.setBounds(525, 50, 125, 55);

		add(Helpbutton);
		add(button2);
		add(button3);
		add(button4);
		setOpaque(false);

		setSize(1500, 700);
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

}