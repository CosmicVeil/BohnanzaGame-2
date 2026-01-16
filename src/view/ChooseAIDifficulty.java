package view;

import javax.swing.*;
import java.awt.*;

/**
 * @author Vishal Parvannan
 **/

@SuppressWarnings("serial")
public class ChooseAIDifficulty extends JPanel {

	private ImageIcon backgroundImage;
	public JButton easyButton;
	public JButton mediumButton;
	public JButton hardButton;

	public ChooseAIDifficulty() {

		backgroundImage = new ImageIcon("src/images/SaranGround.jpg");

		setLayout(null);
		setPreferredSize(new Dimension(1728, 1117));

		easyButton = createButton("Easy AI");
		mediumButton = createButton("Medium AI");
		hardButton = createButton("Hard AI");

		easyButton.setBounds(740, 520, 200, 50);
		mediumButton.setBounds(740, 590, 200, 50);
		hardButton.setBounds(740, 660, 200, 50);

		add(easyButton);
		add(mediumButton);
		add(hardButton);
	}

	private JButton createButton(String text) {
		JButton button = new JButton(text);
		button.setFocusPainted(false);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
		button.setForeground(Color.WHITE);
		button.setFont(new Font("Verdana", Font.BOLD, 24));
		return button;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (backgroundImage != null) {
			g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
		}
	}
}