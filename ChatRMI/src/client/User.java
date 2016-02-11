/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.RemoteException;
import javax.swing.JTextArea;

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
    private JTextArea output;
    
    /**
     * Creates a new User.
     * 
     * @param id Id of the user.
     * @param pseudo Pseudo of the user.
     */
    public User(String id, String pseudo, JTextArea output) {
        this.id = id;
        this.pseudo = pseudo;
        this.output = output;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        output.append(message + "\n");
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
