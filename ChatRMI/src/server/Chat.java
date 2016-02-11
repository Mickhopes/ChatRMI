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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

/**
 * Implentation of ChatInterface which the client will use
 * 
 * @author Mickaël Turnel
 * @author Line Pouvaret
 * @see ChatInterface
 */
public class Chat implements ChatInterface {

    /**
     * List of the user connected.
     * 
     * @see UserInterface
     */
    private HashMap<String, UserInterface> userMap;
    
    /**
     * List of the messages in the Chat.
     * 
     */
    private ArrayList<String> messageList;
    
    /**
     * Id counter for the Chat.
     */
    private int idUser;
    
    /**
     * Password needed to connect to the chat.
     */
    private String password;

    /**
     * Creates a new Chat.
     * 
     * @param password Password needed to connect to the Chat server.
     */
    public Chat(String password) {
        userMap = new HashMap<>();
        messageList = new ArrayList<>();
        idUser = 0;
        this.password = password;
    }
    
    /**
     * Creates a new Chat.
     * Sets the password to connect to the Chat server to "".
     */
    public Chat() {
        this("");
    }

    @Override
    public String getUserId() throws RemoteException {
        return "Client-" + (idUser++);
    }

    @Override
    public boolean register(String id, String pseudo, String host, String password) throws RemoteException {
        if (!password.equals(this.password)) {
            return false;
        }
        
        try {
            System.out.println(host);
            Registry reg = LocateRegistry.getRegistry(host);
            UserInterface user = (UserInterface) reg.lookup(id);

            userMap.put(pseudo, user);

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

            userMap.remove(pseudo, user);

            sendMessage("", pseudo + " has gone offline");
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error on server (unregister): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String pseudo, String message) throws RemoteException {
        if (message.startsWith("/")) {
            String [] parts = message.split(" ");
            String res = "";
            
            switch(parts[0]) {
                case "/help":
                    res = "Available commands: help, who, wisp";
                    break;
                case "/who":
                    res = "Connected user(s): ";

                    for(Entry<String, UserInterface> entry : userMap.entrySet()) {
                        res += entry.getKey() + ", ";
                    }
                    res = res.substring(0, res.length()-2);
                    break;
                case "/wisp":  
                    try {
                        if (userMap.containsKey(parts[1])) {
                            message = message.substring(parts[0].length()+1);
                            message = message.substring(parts[1].length()+1);

                            userMap.get(parts[1]).sendMessage(formatMessage("", "Wisp from " + pseudo + " : " + message));
                            res = "Wisp to " + parts[1] + " : " + message;
                        } else {
                            res = "User " + parts[1] + " is not connected";
                        }
                    } catch(IndexOutOfBoundsException ex) {
                        res = "Usage: /wisp <pseudo> <message>";
                    }
                    break;
                default:
                    res = "Unknown command";
                    break;
            }
            
            userMap.get(pseudo).sendMessage(formatMessage("", res));
        } else {
            // Format our message 
            String msg = formatMessage(pseudo, message);

            // Add it in the list and print it
            messageList.add(msg);
            System.out.println(msg);

            // Send it to all the user connected
            for(Entry<String, UserInterface> entry : userMap.entrySet()) {
                entry.getValue().sendMessage(msg);
            }
        }
    }

    /**
     * Format the message send by a client.
     * The message will have the form "[HH:mm:ss] [pseudo] : [message]".
     * But if the message if a server message it will have the form "[HH:mm:ss] [message]".
     * 
     * @param pseudo The pseudo of the sender.
     * @param message The message sent.
     * @return The formatted message.
     */
    private String formatMessage(String pseudo, String message) {
        String res = new SimpleDateFormat("[HH:mm:ss] ", Locale.FRANCE).format(new Date());
        
        if (!pseudo.equals("")) {
            res += pseudo + " : ";
        }
        
        res += message;
        
        return res;
    }

}
