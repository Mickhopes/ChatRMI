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
import message.Message;

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
     * @see Message
     */
    private ArrayList<Message> messageList;
    
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
    public int register(String id, String pseudo, String host, String password) throws RemoteException {
        if (!password.equals(this.password)) {
            return -1;
        }
        
        if (userMap.containsKey(pseudo)) {
            return -2;
        }
        
        try {
            Registry reg = LocateRegistry.getRegistry(host);
            UserInterface user = (UserInterface) reg.lookup(id);

            userMap.put(pseudo, user);
            
            Message m = new Message("", "", pseudo + " has come online", Message.Type.SYSTEM);
            formatTime(m);

            sendMessage(m);
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error on server (register): " + ex.getMessage());
            ex.printStackTrace();
        }

        return 0;
    }

    @Override
    public void unregister(String id, String pseudo, String host) throws RemoteException {
        try {
            Registry reg = LocateRegistry.getRegistry(host);
            UserInterface user = (UserInterface) reg.lookup(id);

            userMap.remove(pseudo, user);

            Message m = new Message("", "", pseudo + " has gone offline", Message.Type.SYSTEM);
            formatTime(m);

            sendMessage(m);
        } catch (NotBoundException | AccessException ex) {
            System.err.println("Error on server (unregister): " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Message message) throws RemoteException {
        if (message.getMessage().startsWith("/")) {
            String [] parts = message.getMessage().split(" ");
            Message m = new Message("", "", "", Message.Type.SYSTEM);
            
            switch(parts[0]) {
                case "/help":
                    m.setMessage("Available commands: help, who, wisp");
                    break;
                case "/who":
                    String res = "Connected user(s): ";

                    for(Entry<String, UserInterface> entry : userMap.entrySet()) {
                        res += entry.getKey() + ", ";
                    }
                    res = res.substring(0, res.length()-2);
                    
                    m.setMessage(res);
                    break;
                case "/wisp":  
                    try {
                        String msg = message.getMessage();
                        if (userMap.containsKey(parts[1])) {
                            msg = msg.substring(parts[0].length()+1);
                            msg = msg.substring(parts[1].length()+1);
                            
                            m.setMessage(msg);
                            m.setPseudo("Wisp from " + message.getPseudo());
                            formatTime(m);
                            m.setTypeMessage(Message.Type.MESSAGE);
                            
                            userMap.get(parts[1]).sendMessage(m);
                            
                            m.setPseudo("Wisp to " + parts[1]);
                            m.setMessage(msg);
                        } else {
                            m.setMessage("User " + parts[1] + " is not connected");
                        }
                    } catch(IndexOutOfBoundsException ex) {
                        m.setMessage("Usage: /wisp <pseudo> <message>");
                    }
                    break;
                default:
                    m.setMessage("Unknown command");
                    break;
            }
            
            formatTime(m);
            userMap.get(message.getPseudo()).sendMessage(m);
        } else {
            // Format our message 
            Message m = new Message("", message.getPseudo(), message.getMessage(), message.getTypeMessage());
            formatTime(m);

            // Add it in the list and print it
            messageList.add(m);
            System.out.println(m);

            // Send it to all the user connected
            for(Entry<String, UserInterface> entry : userMap.entrySet()) {
                entry.getValue().sendMessage(m);
            }
        }
    }

    /**
     * Format and set the time of a message.
     * The time form "[HH:mm:ss]".
     * 
     * @param Message The message.
     */
    private void formatTime(Message message) {
        message.setTime(new SimpleDateFormat("[HH:mm:ss]", Locale.FRANCE).format(new Date()));
    }

}
