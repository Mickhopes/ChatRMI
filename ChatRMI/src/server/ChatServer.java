/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Main class to launch the Chat server.
 * 
 * @author Mickaël Turnel
 * @author Line Pouvaret
 */
public class ChatServer {
 
    /**
     * Launch the Chat server.
     * 
     * @param args Arguments.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java server.ChatServer <port> [password]");
            return;
        }
        
        Chat c;
        if (args.length != 1) {
            c = new Chat(args[1]);
        } else {
            c = new Chat();
        }
        
        try {
            ChatInterface c_stub = (ChatInterface) UnicastRemoteObject.exportObject(c, 0);
            
            Registry reg = LocateRegistry.getRegistry(/*Integer.parseInt(args[0])*/);
            reg.rebind("Chat", c_stub);
            
            System.out.println("Server ready for the chat !");
        } catch (Exception ex) {
            System.err.println("Error on client: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
