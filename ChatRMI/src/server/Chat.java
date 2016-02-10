/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.User;
import client.UserInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mickhopes
 */
public class Chat implements ChatInterface {
    private ArrayList<UserInterface> userList;
    private ArrayList<String> messageList;
    private int idUser;
    
    public Chat() {
        userList = new ArrayList<>();
        messageList = new ArrayList<>();
        idUser = 0;
    }
    
    @Override
    public String getUserId() throws RemoteException {
        return "Client-" + idUser++;
    }
    
    @Override
    public boolean register(String id, String pseudo, String host) throws RemoteException {
        try {
            Registry reg = LocateRegistry.getRegistry(host);
            UserInterface user = (UserInterface) reg.lookup("User");
            
            userList.add(user);
            
            sendMessage("", pseudo + " is connected");
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error on server (register): " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return true;
    }

    @Override
    public void unregister(String id, String pseudo, String host) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMessage(String pseudo, String message) throws RemoteException {
        messageList.add(message);
        
        for(UserInterface user : userList) {
            user.sendMessage(message);
        }
    }
    
}
