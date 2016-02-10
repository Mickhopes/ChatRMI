/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mickhopes
 */
public class ChatServer {
    
    public static void main(String[] args) {
        try {
            Chat c = new Chat();
            ChatInterface c_stub = (ChatInterface) UnicastRemoteObject.exportObject(c, 0);
            
            Registry reg = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
            reg.bind("Chat", c_stub);
            
            System.out.println("Server for the chat !");
        } catch (Exception ex) {
            System.err.println("Error on client: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
