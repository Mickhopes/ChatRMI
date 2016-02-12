/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import message.Message;

/**
 * Implementation of the UserInterface which the server will use
 * 
 * @author Mickaël Turnel
 * @author Line Pouvaret
 * @see UserInterface
 */
public class User implements UserInterface {
    
    /**
     * The user pseudo.
     */
    private String pseudo;
    
    /**
     * The user id.
     */
    private String id;
    
    /**
     * JTextArea for the output
     */
    private JTextPane output;
    
    /**
     * Creates a new User.
     * 
     * @param id Id of the user.
     * @param pseudo Pseudo of the user.
     */
    public User(String id, String pseudo, JTextPane output) {
        this.id = id;
        this.pseudo = pseudo;
        this.output = output;
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {        
        ChatGUI.appendChat(message, output);
    }

    /**
     * Returns the user's pseudo.
     * 
     * @return Pseudo of the user.
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * Returns the user's id.
     * 
     * @return Id of the user.
     */
    public String getId() {
        return id;
    }

}
