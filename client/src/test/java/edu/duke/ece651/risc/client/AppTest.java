/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.risc.client;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import java.net.UnknownHostException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.parallel.Resources;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.ResourceAccessMode;

class AppTest {

  /**
   * Test the SocketClient sending, receiving and disconnecting
   * 
   * @throws UnknownHostException
   * @throws IOException
   * @throws InterruptedException
   * @throws ClassNotFoundException
   */
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  @Test
  public void test_main() throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
    // set up
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
    InputStream input = getClass().getClassLoader().getResourceAsStream("input.txt");
    assertNotNull(input);
    InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");
    assertNotNull(expectedStream);
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
    // Start a new TestLoopBackServer in a separate thread
    Thread th = make_test_server_thread_helper();
    th.start();
    Thread.sleep(100); // let the *current* thead wait for a while to let the server setup
                       // this is a bit of hack
    // Now the TestLoopBackServer should start to wait for a connection...
    try {
      System.setIn(input);
      System.setOut(out);
      String[] args = { "localhost" };
      App.main(args);
    } finally {
      System.setIn(oldIn);
      System.setOut(oldOut);
    }
    String expected = new String(expectedStream.readAllBytes());
    String actual = bytes.toString();
    assertEquals(expected, actual);
    th.join();
  }

  /**
   * Helper method that creates a new thread to run the test loopback server.
   * 
   * @return a Thread object that the server is running on
   */
  private Thread make_test_server_thread_helper() {
    Thread th = new Thread() {
      @Override
      public void run() {
        try {
          TestServer.main(new String[0]);
        } catch (Exception e) {
        }
      }
    };
    return th;
  }

}
