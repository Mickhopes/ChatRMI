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
    
    // Method to connect/disconnect the client
    public String register(String pseudo, String host) throws RemoteException;
    public void unregister(String pseudo, String host) throws RemoteException;
    
    // Method to send a message to the server
    public void sendMessage(String id, String message) throws RemoteException;
}
