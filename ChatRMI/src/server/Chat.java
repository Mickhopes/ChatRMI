/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.UserInterface;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author mickhopes
 */
public class Chat implements ChatInterface {
    private ArrayList<UserInterface> userList;
    private ArrayList<String> messageList;
    private int idUser;
    
        userList = new ArrayList<>();
        messageList = new ArrayList<>();
        idUser = 0;
    }
    
    @Override
    public String getUserId() throws RemoteException {
        return "Client-" + (idUser++);
    }
    
    @Override
    public boolean register(String id, String pseudo, String host, String password) throws RemoteException {
        try {
            Registry reg = LocateRegistry.getRegistry(host);
            UserInterface user = (UserInterface) reg.lookup(id);

            userList.add(user);

            sendMessage("", pseudo + " has come online");
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error on server (register): " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return true;
    }

    @Override
    public void unregister(String id, String pseudo, String host) throws RemoteException {
        try {
            Registry reg = LocateRegistry.getRegistry(host);
            UserInterface user = (UserInterface) reg.lookup(id);

            userList.remove(user);

            sendMessage("", pseudo + " has gone offline");
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error on server (unregister): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String pseudo, String message) throws RemoteException {
        // Format our message 
        String msg = formatMessage(pseudo, message);
        
        // Add it in the list and print it
        messageList.add(msg);
        System.out.println(msg);

        // Send it to all the user connected
        for (UserInterface user : userList) {
            user.sendMessage(msg);
        }
    }
    
    private String formatMessage(String pseudo, String message) {
        String res = new SimpleDateFormat("[HH:mm:ss] ", Locale.FRANCE).format(new Date());
        
        if (!pseudo.equals("")) {
            res += pseudo + " : ";
        }
        
        res += message;
        
        return res;
    }

}
