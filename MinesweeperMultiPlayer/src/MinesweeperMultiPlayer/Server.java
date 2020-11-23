package MinesweeperMultiPlayer;

// Server.java: The server accepts data from the client, processes it
// and returns the result back to the client
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame {
  // Text area for displaying contents
  private JTextArea jta = new JTextArea();
   ServerSocket serverSocket;
   Socket connectToClient1;
   Socket connectToClient2;
   DataInputStream isFromClient1;
   DataOutputStream osToClient1;
   DataInputStream isFromClient2;
   DataOutputStream osToClient2;
   ObjectOutputStream sendML1;
   ObjectOutputStream sendML2;
   GameLogic ML=new GameLogic();
   static int left=1;
   static int right=2;


  public static void main(String[] args) {
    new Server();
  }

  public Server() {
    // Place text area on the frame
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(jta), BorderLayout.CENTER);

    setTitle("Server");
    setSize(500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true); // It is necessary to show the frame here!

    try {
      // Create a server socket
       serverSocket = new ServerSocket(8000);
      jta.append("Server started at " + new Date() + '\n');

      // Listen for a connection request from client1
       connectToClient1 = serverSocket.accept();

      // Create data input and output streams
       isFromClient1 = new DataInputStream(
        connectToClient1.getInputStream());
       osToClient1 = new DataOutputStream(
        connectToClient1.getOutputStream());
        sendML1 =new ObjectOutputStream(connectToClient1.getOutputStream());

        osToClient1.writeInt(1);
        osToClient1.flush();
        sendML1.writeObject(ML);
        sendML1.flush();


        // Listen for a connection request from client2
       connectToClient2 = serverSocket.accept();

      // Create data input and output streams
       isFromClient2 = new DataInputStream(
        connectToClient2.getInputStream());
       osToClient2 = new DataOutputStream(
        connectToClient2.getOutputStream());
        sendML2 =new ObjectOutputStream(connectToClient2.getOutputStream());

        osToClient2.writeInt(2);
        osToClient2.flush();
        sendML2.writeObject(ML);
        sendML2.flush();


      int i;
      int j;
      int lr;
      while (true) {
        // Receive radius from the client
         i=isFromClient1.readInt();
         j=isFromClient1.readInt();
         lr=isFromClient1.readInt();
         if(lr==left)
             Open(i,j);
         else if(lr==right)
         {
             ML.set_isFlag(i,j,1);
             ML.issucc();
         }
         osToClient2.writeInt(i);
         osToClient2.writeInt(j);
         osToClient2.writeInt(lr);
         osToClient2.flush();


         i=isFromClient2.readInt();
         j=isFromClient2.readInt();
         lr=isFromClient2.readInt();
         if(lr==left)
             Open(i,j);
         else if(lr==right)
         {
             ML.set_isFlag(i,j,2);
             ML.issucc();
         }
         osToClient1.writeInt(i);
         osToClient1.writeInt(j);
         osToClient1.writeInt(lr);
         osToClient1.flush();
        
      }
    }
    catch(IOException ex) {
      System.err.println(ex);
    }
  }
   public void Open(int i, int j)
 {
     if(ML.get_isFlag(i, j)==0)
     {
                        if(ML.get_label(i, j)==-1)
                        {
                            //fail=true;
                        }
                        else
                        {
                            ML.openCell(i, j);
                            ML.issucc();
                        }
     }
 }
}