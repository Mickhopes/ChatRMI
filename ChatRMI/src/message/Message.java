/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.Serializable;

/**
 * Defines a message for the chat.
 * 
 * @author Mickaël Turnel
 */
public class Message implements Serializable {
    
    /**
     * Boolean that tells if the message is a system message.
     */
    private boolean systemMessage;
    
    /**
     * String that contains the time that the message has been sent.
     */
    private String time;
    
    /**
     * String that contains the pseudo of the sender.
     */
    private String pseudo;
    
    /**
     * String that contains the message.
     */
    private String message;
    
    /**
     * Creates a new message.
     * 
     * @param time The time of the message.
     * @param pseudo The pseudo of the sender.
     * @param message The message.
     * @param systemMessage true if the message is a system message.
     */
    public Message(String time, String pseudo, String message, boolean systemMessage) {
        this.time = time;
        this.pseudo = pseudo;
        this.message = message;
        this.systemMessage = systemMessage;
    }
    
    /**
     * Creates a new message.
     * 
     * @param time The time of the message.
     * @param pseudo The pseudo of the sender.
     * @param message The message.
     */
    public Message(String time, String pseudo, String message) {
        this(time, pseudo, message, false);
    }

    /**
     * Tells if the message is a system message.
     * 
     * @return true if this is a system message.
     */
    public boolean isSystemMessage() {
        return systemMessage;
    }

    /**
     * Sets if this message is a system message.
     * 
     * @param systemMessage true if this is a system message.
     */
    public void setSystemMessage(boolean systemMessage) {
        this.systemMessage = systemMessage;
    }

    /**
     * Returns the time of the message.
     * 
     * @return Time of the message.
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the time of the message.
     * 
     * @param time Time of the message.
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Returns the pseudo of the sender.
     * 
     * @return Pseudo of the sender.
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Sets the pseudo of the sender.
     * 
     * @param pseudo Pseudo of the sender.
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * Returns the message.
     * 
     * @return The message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     * 
     * @param message Message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return this.time + (this.pseudo.equals("") ? " " : " " + this.pseudo + " : ") + this.message;
    }
    
}
