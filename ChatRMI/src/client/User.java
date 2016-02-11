/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ChatInterface;

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
     * Creates a new User.
     * 
     * @param id Id of the user.
     * @param pseudo Pseudo of the user.
     */
    public User(String id, String pseudo) {
        this.id = id;
        this.pseudo = pseudo;
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        // Get the message and show it
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
