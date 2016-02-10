/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author mickhopes
 */
public interface UserInterface extends Remote {
    
    // Method to send a notification to the client when a new message has been sent
    public void sendMessage(String message) throws RemoteException;
}
