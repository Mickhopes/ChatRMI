/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import message.Message;

/**
 * Interface that defines a chat for the remote connection.
 * 
 * @author Mickaël Turnel
 * @author Line Pouvaret
 */
public interface ChatInterface extends Remote {
    
    /**
     * Get the next user id available.
     * 
     * @return The id for the user.
     * @throws RemoteException 
     */
    public String getUserId() throws RemoteException;
    
    /**
     * Register to the chat server.
     * 
     * @param id Id of the user.
     * @param pseudo Pseudo of the user.
     * @param host IP address of the user.
     * @param password Password sent by the user.
     * @return 0 if registration is ok, -1 if wrong password and -2 if pseudo already exists.
     * @throws RemoteException 
     */
    public int register(String id, String pseudo, String host, String password) throws RemoteException;
    
    /**
     * Unregister to the chat server.
     * 
     * @param id Id of the user.
     * @param pseudo Pseudo of the user.
     * @param host IP address of the user.
     * @throws RemoteException 
     */
    public void unregister(String id, String pseudo, String host) throws RemoteException;
    
    /**
     * Send a message to the chat server.
     * 
     * @param message Message sent.
     * @throws RemoteException 
     */
    public void sendMessage(Message message) throws RemoteException;
}
