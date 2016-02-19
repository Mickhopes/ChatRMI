/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.UserInterface;
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
     * Register to the chat server.
     * 
     * @param pseudo Pseudo of the user to register.
     * @param password Password sent by the user.
     * @param user User to register.
     * @return 0 if registration is ok, -1 if wrong password and -2 if pseudo already exists.
     * @throws RemoteException 
     */
    public int register(String pseudo, String password, UserInterface user) throws RemoteException;
    
    /**
     * Unregister to the chat server.
     * 
     * @param pseudo Pseudo of the user to unregister.
     * @throws RemoteException 
     */
    public void unregister(String pseudo) throws RemoteException;
    
    /**
     * Send a message to the chat server.
     * 
     * @param message Message sent.
     * @throws RemoteException 
     */
    public void sendMessage(Message message) throws RemoteException;
    
    public void showConnectedUser(String pseudo) throws RemoteException;
    
    public void sendWhisp(Message message) throws RemoteException;
    
    public void showCommands(String pseudo) throws RemoteException;
}
