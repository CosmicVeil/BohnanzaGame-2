package model;

/**
 * @author Mohan Dixit
 */

public interface TurnInterface {

	void plantBeans(int beanField);

	void discardCard(int index);

	void drawCard(Card card);

	void harvest(int beanField);

	void buyField(int beanField);

}