/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.rmi.Remote;
import java.rmi.RemoteException;
import message.Message;

/**
 * Interface that defines a user for the remote connection.
 * 
 * @author Mickaël Turnel
 * @author Line Pouvaret
 */
public interface UserInterface extends Remote {
    
    /**
     * Send a message to the user.
     * 
     * @param message Message to send.
     * @throws RemoteException 
     */
    public void sendMessage(Message message) throws RemoteException;
}
