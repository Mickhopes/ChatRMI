/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.UserInterface;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * Registry for the rmi connection
     */
    private Registry registry;

    /**
     * Creates a new Chat.
     * 
     * @param password Password needed to connect to the Chat server.
     */
    public Chat(String password, int port) {
        userMap = new HashMap<>();
        messageList = new ArrayList<>();
        idUser = 0;
        this.password = password;
        
        try {
            ChatInterface c_stub = (ChatInterface) UnicastRemoteObject.exportObject(this, 0);
            registry = LocateRegistry.getRegistry(port);
            registry.rebind("Chat", c_stub);
        } catch (RemoteException ex) {
            System.err.println("Error on server (registry): " + ex.getMessage());
            ex.printStackTrace();
            System.exit(-1);
        }
        
        try (
            InputStream fis = new FileInputStream("historic.txt");
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length > 1) {
                    Message m = new Message(parts[0], "", "", Message.Type.MESSAGE);
                    if (parts[2].equals(":")) {
                        m.setPseudo(parts[1]);
                        m.setMessage(line.substring(parts[0].length()+1).substring(parts[1].length()+3));
                    } else {
                        m.setMessage(line.substring(parts[0].length()+1));
                        m.setTypeMessage(Message.Type.SYSTEM);
                    }
                    messageList.add(m);
                }
            }
        } catch(FileNotFoundException ex) {
            try(FileWriter f = new FileWriter("historic.txt")) {
                System.out.println("File \"historic.txt\" created.");
            } catch (IOException ex1) {
                System.err.println("Unable to create file \"historic.txt\" created.");
            }
        } catch (IOException ex) {
            System.err.println("Error on server (open historic) : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Creates a new Chat.
     * Sets the port to connect to the default port (1099).
     * 
     * @param password Password to connect.
     */
    public Chat(String password) {
        this(password, 1099);
    }
    
    /**
     * Creates a new Chat.
     * Sets the password to "" and the port to the default port 1099.
     */
    public Chat() {
        this("", 1099);
    }

    @Override
    public String getUserId() throws RemoteException {
        return "Client-" + (idUser++);
    }

    @Override
    public int register(String id, String pseudo, String password) throws RemoteException {
        if (!password.equals(this.password)) {
            return -1;
        }
        
        if (userMap.containsKey(pseudo)) {
            return -2;
        }
        
        try {
            UserInterface user = (UserInterface) registry.lookup(id);

            userMap.put(pseudo, user);
            
            int i = messageList.size() < 30 ? 0 : messageList.size()-30;
            while (i < messageList.size()) {
                Message m = messageList.get(i++);
                Message.Type t = m.getTypeMessage();
                m.setTypeMessage(t == Message.Type.MESSAGE ? Message.Type.OLD_MESSAGE : Message.Type.OLD_SYSTEM);
                user.sendMessage(m);
                m.setTypeMessage(t);
            }
            
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
    public void unregister(String id, String pseudo) throws RemoteException {
        try {
            UserInterface user = (UserInterface) registry.lookup(id);

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
                    m.setMessage("Available commands: help, get, who, wisp");
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
                case "/get":
                    try {
                        if (!messageList.isEmpty()) {
                            formatTime(m);
                            m.setMessage("--- Last " + parts[1] + " messages : ");
                            userMap.get(message.getPseudo()).sendMessage(m);
                            
                            int val = Integer.parseInt(parts[1]);

                            int i = messageList.size() >= val ? messageList.size()-val : 0;
                            while (i < messageList.size()) {
                                userMap.get(message.getPseudo()).sendMessage(messageList.get(i++));
                            }

                            m.setMessage("--- End of get.");
                        } else {
                            m.setMessage("There is no messages to get...");
                        }
                    } catch(IndexOutOfBoundsException | NumberFormatException ex) {
                        m.setMessage("Usage: /get <number of messages>");
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
            
            try(FileWriter f = new FileWriter("historic.txt", true);) {
                f.append(m.toString() + "\n");
            } catch (IOException ex) {
                Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
            }

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
        message.setTime(new SimpleDateFormat("[dd/MM/yy-HH:mm:ss]", Locale.FRANCE).format(new Date()));
    }

}
