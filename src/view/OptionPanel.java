package view;

import javax.swing.*;
import java.awt.*;

/**
 * @author Vishal Parvannan
 */

@SuppressWarnings("serial")
public class OptionPanel extends JPanel {

    private ImageIcon backgroundImage;
    public JButton onePlayerButton = createButton("One Player");
    public JButton twoPlayerButton = createButton("Two Player");
    public JButton instructionButton = createButton("Instruction");

    public OptionPanel() {

        setLayout(null); 
        
		backgroundImage = new ImageIcon("src/images/SaranGround.jpg");
        Image scaledImage = backgroundImage.getImage().getScaledInstance(1728, 1117, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);

        JLabel titlepagelabel1 = new JLabel(resizedIcon);
        titlepagelabel1.setBounds(0, 0, 1728, 1117);
        add(titlepagelabel1);

        onePlayerButton.setBounds(740, 525, 200, 50);
        twoPlayerButton.setBounds(740, 595,200, 50);
        instructionButton.setBounds(740, 665, 200, 50);

        titlepagelabel1.add(onePlayerButton);
        titlepagelabel1.add(twoPlayerButton);
        titlepagelabel1.add(instructionButton);
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
   
}