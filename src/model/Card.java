package model;

import javax.swing.JButton;

import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author Saran Rajagopal
 */

@SuppressWarnings("serial")
public class Card extends JButton {
	private String beanName;
	private int[] sellingPrice;
	private int totalCopies;
	private String link;

	public Card(String beanName, int[] sellingPrice, int totalCopies) {
		super();
		this.beanName = beanName;
		this.sellingPrice = sellingPrice;
		this.totalCopies = totalCopies;
		this.link = "src/images/" + beanName + ".png";
		this.setIcon(cardView());
	}

	public ImageIcon cardView() {
		try {
			String path = "/images/" + beanName + ".png";
			Image img = ImageIO.read(getClass().getResource(path));
			return new ImageIcon(img);
		} catch (Exception e) {
			System.out.println("IMAGE NOT FOUND: " + beanName);
			e.printStackTrace();
			return null;
		}
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
		this.link = "src/images/" + beanName + ".png";
	}

	public int[] getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(int[] sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public int getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(int totalCopies) {
		this.totalCopies = totalCopies;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		return this.beanName.equals(other.getBeanName());
	}
}