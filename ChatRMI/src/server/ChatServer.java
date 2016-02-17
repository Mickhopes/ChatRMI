/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

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
            System.out.println("Usage: java server.ChatServer <password> [port]");
            return;
        }
        
        Chat c;
        try {
            if (args.length == 2) {
                c = new Chat(args[0], Integer.parseInt(args[1]));
            } else {
                c = new Chat(args[0]);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Usage: java server.ChatServer <password> [port]");
            return;
        }
        
        System.out.println("Server ready for the chat !");
    }
    
}
