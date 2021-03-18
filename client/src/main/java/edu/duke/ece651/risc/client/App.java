/**
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is the App class for client project
 */
public class App {
    /**
     * This is the main method of the whole project.
     * 
     * A client is created to handle connection with server.
     * A BufferedReader is created to handle input.
     * A player is created for play logic.
     * 
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args) throws IOException,ClassNotFoundException{
        SocketClient client=new SocketClient(12345,"localhost");
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        GamePlayer<String> player=new V1GamePlayer<String>(-1, client, input, System.out);
        player.initGame();
        player.pickTerritory();
        player.deployUnits();
        player.doPlayPhase();
    }
}
