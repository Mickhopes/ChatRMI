/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mickhopes
 */
public interface ChatInterface extends Remote {
    
    // Method to get the user id needed for the chat to work
    public String getUserId() throws RemoteException;
    
    // Method to connect/disconnect the user
    public boolean register(String id, String pseudo, String host) throws RemoteException;
    public void unregister(String id, String pseudo, String host) throws RemoteException;
    
    // Method to send a message to the server
    public void sendMessage(String pseudo, String message) throws RemoteException;
}
