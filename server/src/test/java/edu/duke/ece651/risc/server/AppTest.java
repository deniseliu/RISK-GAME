/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import edu.duke.ece651.risc.shared.*;
import edu.duke.ece651.risc.client.*;

class AppTest {

  @Mock
  private Random rdmMock;

  @Test
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  public void test_App()
      throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException, BrokenBarrierException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);

    // Get an InputStream from serverInput.txt file
    InputStream serverInput = getClass().getClassLoader().getResourceAsStream("serverInput.txt");
    assertNotNull(serverInput);
    // Get an InputStream from serverOutput.txt file
    InputStream expectedServerOutput = getClass().getClassLoader().getResourceAsStream("serverOutput.txt");
    assertNotNull(expectedServerOutput);

    // Now get the input and output for four game players
    InputStream player0Input = getClass().getClassLoader().getResourceAsStream("player0Input.txt"); // what did the
                                                                                                    // player type in
                                                                                                    // command line
    assertNotNull(player0Input);
    InputStream expectedPlayer0Output = getClass().getClassLoader().getResourceAsStream("player0Output.txt"); // what
                                                                                                              // player
                                                                                                              // 1
                                                                                                              // client
                                                                                                              // supposed
                                                                                                              // to
                                                                                                              // print
                                                                                                              // to
                                                                                                              // command
                                                                                                              // line
    assertNotNull(expectedPlayer0Output);
    ByteArrayOutputStream p0bytes = new ByteArrayOutputStream();
    PrintStream p0Out = new PrintStream(p0bytes, true); // what did player 1 client really print

    InputStream player1Input = getClass().getClassLoader().getResourceAsStream("player1Input.txt");
    assertNotNull(player1Input);
    InputStream expectedPlayer1Output = getClass().getClassLoader().getResourceAsStream("player1Output.txt");
    assertNotNull(expectedPlayer1Output);
    ByteArrayOutputStream p1bytes = new ByteArrayOutputStream();
    PrintStream p1Out = new PrintStream(p1bytes, true);

    InputStream player2Input = getClass().getClassLoader().getResourceAsStream("player2Input.txt");
    assertNotNull(player2Input);
    InputStream expectedPlayer2Output = getClass().getClassLoader().getResourceAsStream("player2Output.txt");
    assertNotNull(expectedPlayer2Output);
    ByteArrayOutputStream p2bytes = new ByteArrayOutputStream();
    PrintStream p2Out = new PrintStream(p2bytes, true);

    InputStream player3Input = getClass().getClassLoader().getResourceAsStream("player3Input.txt");
    assertNotNull(player3Input);
    InputStream expectedPlayer3Output = getClass().getClassLoader().getResourceAsStream("player3Output.txt");
    assertNotNull(expectedPlayer3Output);
    ByteArrayOutputStream p3bytes = new ByteArrayOutputStream();
    PrintStream p3Out = new PrintStream(p3bytes, true);

    // Now create 4 thread for all our four game players
    Thread p0Thread = new Thread(new TestGamePlayer(player0Input, p0Out));
    Thread p1Thread = new Thread(new TestGamePlayer(player1Input, p1Out));
    Thread p2Thread = new Thread(new TestGamePlayer(player2Input, p2Out));
    Thread p3Thread = new Thread(new TestGamePlayer(player3Input, p3Out));

    // Hold the current System.in and System.out
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
    // Change the in/out stream of App to input.txt/output.txt
    try {
      System.setIn(serverInput);
      System.setOut(out);
      Thread appThread = make_server_thread_helper();
      appThread.start(); // start the server

      Thread.sleep(200); // let the server have enough time to setup

      // Let the players connect to the server
      p0Thread.start();

      Thread.sleep(500); // wait for the first player decide the # of total players and send it to the
                         // server

      p1Thread.start();
      p2Thread.start();
      p3Thread.start();

      appThread.join();
      p0Thread.join();
      p1Thread.join();
      p2Thread.join();
      p3Thread.join();
    } finally {
      // Ensure that we restore to the old System.in/out
      System.setIn(oldIn);
      System.setOut(oldOut);
    }
    String expectedServerOutputString = new String(expectedServerOutput.readAllBytes());
    String actualServerOutputSting = bytes.toString();
    assertEquals(expectedServerOutputString, actualServerOutputSting);
  }

  @Test
  public void test_main() throws InterruptedException {
    // Start a new TestLoopBackServer in a separate thread
    Thread server = make_server_thread_helper();
    server.start();

    // Let the thread that excute teh test_main sleep for a while,
    // wait for the server setup
    Thread.sleep(200);

    Thread th0 = make_test_player_thread_helper("0");
    th0.start();

    Thread.sleep(300);

    Thread th1 = make_test_player_thread_helper("1");
    th1.start();

    Thread.sleep(300); // a bit of hacky

    server.join();
    th0.join();
    th1.join();
  }

  private Thread make_server_thread_helper() {
    Thread th = new Thread() {
      @Override
      public void run() {
        try {
          App.main(new String[0]);
          // System.out.println("test finished!");
        } catch (Exception e) {
        }
      }
    };
    return th;
  }

  private Thread make_test_player_thread_helper(String id) {
    Thread th = new Thread() {
      @Override
      public void run() {
        try {
          String[] args = { id };
          TestPlayer.main(args);
        } catch (Exception e) {
        }
      }
    };
    return th;
  }
}
