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
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
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
        if (message == null || output == null) {
            return;
        }
        
        StyledDocument doc = output.getStyledDocument();
        Style s = output.addStyle("Color", null);
        if (message.getTypeMessage() == Message.Type.OLD_MESSAGE || message.getTypeMessage() == Message.Type.OLD_SYSTEM) {
            StyleConstants.setForeground(s, Color.GRAY);
        } else {
            StyleConstants.setForeground(s, Color.GREEN);
        }
        
        try {
            if (!output.getText().isEmpty()) {
                doc.insertString(doc.getLength(), "\n", null);
            }
            
            if (!message.getTime().equals("")) {
                doc.insertString(doc.getLength(), message.getTime() + " ", s);
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (message.getTypeMessage() == Message.Type.OLD_MESSAGE || message.getTypeMessage() == Message.Type.OLD_SYSTEM) {
            StyleConstants.setForeground(s, Color.GRAY);
        } else {
            if (message.getPseudo().startsWith("Wisp")) {
                StyleConstants.setForeground(s, Color.PINK);
            } else {
                StyleConstants.setForeground(s, Color.BLUE);
            }
        }
        
        try {
            if (message.getTypeMessage() == Message.Type.MESSAGE || message.getTypeMessage() == Message.Type.OLD_MESSAGE) {
                doc.insertString(doc.getLength(), message.getPseudo() + " : ", s);
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        switch(message.getTypeMessage()) {
            case MESSAGE:
            case APPLICATION:
                StyleConstants.setForeground(s, Color.BLACK);
                break;
            case SYSTEM:
                StyleConstants.setForeground(s, Color.ORANGE);
                break;
            case ERROR:
                StyleConstants.setForeground(s, Color.RED);
                break;
            case OLD_MESSAGE:
            case OLD_SYSTEM:
                StyleConstants.setForeground(s, Color.GRAY);
                break;
        }
        
        try {
            doc.insertString(doc.getLength(), message.getMessage(), s);
        } catch (BadLocationException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
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
