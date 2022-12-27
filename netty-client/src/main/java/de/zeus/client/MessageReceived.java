package de.zeus.client;

/**
 * This class is a listener for incoming messages. It can be used for external commands
 *
 * @author ZeusSeinGrossopa
 */
public interface MessageReceived {
    /**
     * This method is called when a message is received
     *
     * @param message the message
     */
    void onMessageReceived(String message);
}