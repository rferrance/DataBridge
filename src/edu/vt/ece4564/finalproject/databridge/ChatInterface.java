package edu.vt.ece4564.finalproject.databridge;

//Interface between MainActivity and ChatFragment.
public interface ChatInterface {
	void sendMessage(String aMessage);
	void receiveMessage();
}
