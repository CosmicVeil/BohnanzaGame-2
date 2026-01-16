package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.*;

/**
 * @author Saran Rajagopal
 */

@SuppressWarnings("serial")
public class BohnanzaGameFrame extends JFrame {
	ImageIcon backgroundIcon = new ImageIcon("src/images/BackgroundB.jpg");
	Image scaledImage = backgroundIcon.getImage().getScaledInstance(1700, 1000, Image.SCALE_SMOOTH);
	ImageIcon resizedIcon = new ImageIcon(scaledImage);

	private HelpPanel helpPanel = new HelpPanel();
	public OptionPanel optionPanel = new OptionPanel();
	public ChooseAIDifficulty aiDifficultyPanel = new ChooseAIDifficulty();
	private PlayerPanel playerPanel1 = new PlayerPanel();
	private PlayerPanel playerPanel2 = new PlayerPanel();
	private PlayerPanel currentPlayerPanel;
	private OfferAreaPanel offerAreaPanel = new OfferAreaPanel();
	public JLabel currentStep = new JLabel("Plant & Discard");
	public JLabel titlepagelabel = new JLabel(resizedIcon);
	public boolean onePlayer;

	public BohnanzaGameFrame() {
		TitlePage();
	}

	public void Settings() {
		Settingspage();
	}

	public void StartGame() {
		displayGameBoard();
	}

	public BohnanzaGameFrame(HelpPanel helpPanel, PlayerPanel playerPanel1, PlayerPanel playerPanel2,
			OfferAreaPanel offerAreaPanel) {
		super();
		this.helpPanel = helpPanel;
		this.playerPanel1 = playerPanel1;
		this.playerPanel2 = playerPanel2;
		this.offerAreaPanel = offerAreaPanel;
	}

	public void displayGameBoard() {
		setTitle("Bohnanza Game!");
		setSize(1600, 910);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		getContentPane().removeAll();

		JLabel backgroundLabel = new JLabel(resizedIcon);
		backgroundLabel.setBounds(0, 0, 1600, 910);
		backgroundLabel.setLayout(null);
		setContentPane(backgroundLabel);

		currentStep.setBounds(100, 20, 400, 50);
		currentStep.setFont(new Font("Verdana", Font.BOLD, 25));
		currentStep.setForeground(Color.WHITE);
		backgroundLabel.add(currentStep);

		helpPanel.setBounds(450, 0, 700, 150);
		playerPanel1.setBounds(50, 500, 652, 360);
		playerPanel2.setBounds(850, 500, 652, 360);
		offerAreaPanel.setOpaque(false);
		offerAreaPanel.setBounds(400, 200, 900, 300);

		backgroundLabel.add(helpPanel);
		backgroundLabel.add(playerPanel1);
		backgroundLabel.add(playerPanel2);
		backgroundLabel.add(offerAreaPanel);

		currentPlayerPanel = playerPanel1;

		revalidate();
		repaint();
		setVisible(true);
	}

	public void TitlePage() {
		setTitle("Bohnanza Game!");
		setSize(1728, 1117);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		ImageIcon backgroundIcon = new ImageIcon("src/images/mohan.jpg");
		Image scaledImage = backgroundIcon.getImage().getScaledInstance(1728, 1117, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(scaledImage);

		titlepagelabel = new JLabel(resizedIcon);
		titlepagelabel.setBounds(0, 0, getWidth(), getHeight());
		titlepagelabel.setLayout(null);
		add(titlepagelabel);
		setVisible(true);
	}

	public void Settingspage() {
		setTitle("Bohnanza Game!");
		setSize(1728, 1117);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		getContentPane().removeAll();

		optionPanel.setBounds(0, 0, 1728, 1117);
		add(optionPanel);

		revalidate();
		repaint();
		setVisible(true);
	}

	public void AIDifficultyPage() {
		setTitle("Bohnanza Game!");
		setSize(1728, 1117);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		getContentPane().removeAll();

		aiDifficultyPanel.setBounds(0, 0, 1728, 1117);
		add(aiDifficultyPanel);

		revalidate();
		repaint();
		setVisible(true);
	}

	public void showTextFilePopup() {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream("/textfile/rule.txt")));

			StringBuilder text = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null) {
				text.append(line).append("\n");
			}
			reader.close();

			JTextArea textArea = new JTextArea(text.toString());
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);

			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setPreferredSize(new Dimension(500, 400));

			JOptionPane.showMessageDialog(this, scrollPane, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PlayerPanel getCurrentPlayerPanel() {
		return currentPlayerPanel;
	}

	public HelpPanel getHelpPanel() {
		return helpPanel;
	}

	public void setHelpPanel(HelpPanel helpPanel) {
		this.helpPanel = helpPanel;
	}

	public PlayerPanel getPlayerPanel1() {
		return playerPanel1;
	}

	public void setPlayerPanel1(PlayerPanel playerPanel1) {
		this.playerPanel1 = playerPanel1;
	}

	public PlayerPanel getPlayerPanel2() {
		return playerPanel2;
	}

	public void setPlayerPanel2(PlayerPanel playerPanel2) {
		this.playerPanel2 = playerPanel2;
	}

	public OfferAreaPanel getOfferAreaPanel() {
		return offerAreaPanel;
	}

	public void setOfferAreaPanel(OfferAreaPanel offerAreaPanel) {
		this.offerAreaPanel = offerAreaPanel;
	}

	public void setCurrentPlayerPanel(PlayerPanel currentPlayerPanel) {
		this.currentPlayerPanel = currentPlayerPanel;
	}

	public Object getBohnanzaGameFrame() {
		return null;
	}

	public OptionPanel getOptionPanel() {
		return optionPanel;
	}

	public void setOptionPanel(OptionPanel optionPanel) {
		this.optionPanel = optionPanel;
	}
}