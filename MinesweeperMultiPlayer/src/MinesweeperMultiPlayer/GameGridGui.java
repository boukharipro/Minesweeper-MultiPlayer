/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MinesweeperMultiPlayer;

/**
 *
 * @author BAU
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class GameGridGui extends JPanel implements ActionListener , MouseListener
{
 private JPanel time_flag;
 private JLabel lflag;
 private JLabel lttime;
 private JLabel ltflag;
 private JPanel grid;
 private JMenuBar mb;
 private JMenu file;
 private JMenu help;
 private JMenuItem new_game;
 private JButton squares[][];
 private JLabel labels[][];
 public boolean fail=false;
 public boolean succ=false;
 private GameLogic ML;
 int flag = 0;
 Timer timer = new Timer("Printer");
 MyTimer t = new MyTimer();

 static int left=1;
 static int right=2;
 int pn;
 boolean enabled_flag=false;
 DataOutputStream osToServer;
 DataInputStream isFromServer;
 Socket connectToServer;
 ObjectInputStream readML;


public GameGridGui()
{
    timer.schedule(t, 0, 1000);

     grid=new JPanel();
     time_flag=new JPanel();
     time_flag.setLayout(new GridLayout(1,5));
     grid.setSize(400,400);
     grid.setLayout(new GridLayout(10,10));
     squares = new JButton[10][10];
     labels = new JLabel[10][10];
     mb =new JMenuBar();
     file=new JMenu("File");
     help= new JMenu("Help");
     new_game=new JMenuItem("New Game");
     file.add(new_game);
     mb.add(file);
     mb.add(help);
     lflag=new JLabel();
     lflag.setHorizontalAlignment(0);
     lttime=new JLabel("Time: ");
     lttime.setHorizontalAlignment(0);
     ltflag=new JLabel("Flags: ");
     ltflag.setHorizontalAlignment(0);
     time_flag.add(ltflag);
     time_flag.add(lflag);
     time_flag.add(new JLabel(""));
     time_flag.add(lttime);
     time_flag.add(t.ltime);
     this.setLayout(new BorderLayout());
     new_game.addActionListener(this);



     try {
      // Create a socket to connect to the server
      connectToServer = new Socket("localhost", 8000);
      //Socket connectToServer = new Socket("130.254.204.36", 8000);
      //Socket connectToServer = new Socket(
      //  "drake.Armstrong.edu", 8000);

      // Create an input stream to receive data from the server
      isFromServer = new DataInputStream(connectToServer.getInputStream());

      // Create an output stream to send data to the server
      osToServer =new DataOutputStream(connectToServer.getOutputStream());
      readML=new ObjectInputStream(connectToServer.getInputStream());



      pn=isFromServer.readInt();
      if(pn==1)
          enabled_flag=true;
            try {
                ML = (GameLogic) readML.readObject();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameGridGui.class.getName()).log(Level.SEVERE, null, ex);
            }

HandleAClient t=new HandleAClient(connectToServer);
t.start();
    }
    catch (IOException ex) {
     System.out.println(ex.toString() + '\n');
    }


     buildButtons();

}

private void buildButtons()
{
    grid.removeAll();

    this.removeAll();

    this.add(mb,BorderLayout.NORTH);
    this.add(grid,BorderLayout.CENTER);
    this.add(time_flag,BorderLayout.SOUTH);
    ImageIcon imgMine = new ImageIcon("mine.png");
    ImageIcon imgFlag1 = new ImageIcon("flag1.png");
    ImageIcon imgFlag2 = new ImageIcon("flag2.png");
    lflag.setText(""+ML.get_flags(pn));




     for(int i=0;i<10;i++){
          for(int j=0;j<10;j++){
               squares[i][j] = new JButton();
               squares[i][j].setSize(400,400);

               labels[i][j] = new JLabel();
               labels[i][j].setSize(400,400);
               labels[i][j].setHorizontalAlignment(0);
               if(enabled_flag)
                   squares[i][j].setEnabled(true);
               else
                   squares[i][j].setEnabled(false);

               if(fail||ML.succ!=0)
               {
                    grid.add(labels[i][j]);
                    labels[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                    if(ML.get_label(i, j)==-1) {
                         labels[i][j].setIcon(imgMine);
                     }
                    else if(ML.get_label(i, j)==0)
                        labels[i][j].setText("  ");
                    else
                        labels[i][j].setText(""+ML.get_label(i, j));
               }
               else if(ML.get_isopen(i, j))
               {
                  grid.add(labels[i][j]);
                  labels[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                  labels[i][j].addMouseListener(this);
                  if(ML.get_label(i, j)==-1) {
                       labels[i][j].setIcon(imgMine);
                   }
                  else if(ML.get_label(i, j)==0)
                      labels[i][j].setText("  ");
                  else
                      labels[i][j].setText(""+ML.get_label(i, j));
              }
               else {
                  grid.add(squares[i][j]);
                  squares[i][j].addActionListener(this);
                  squares[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
                  squares[i][j].addMouseListener(this);
                  if(ML.get_isFlag(i,j)==1)
                      squares[i][j].setIcon(imgFlag1);
                  if(ML.get_isFlag(i,j)==2)
                      squares[i][j].setIcon(imgFlag2);

              }
          }
     }
    if(ML.succ==1&&flag==0&&pn==1)
    {
        flag=1;
        t.stop=true;
        JOptionPane.showMessageDialog(this,"Congratulations, you won!!","Game Won",JOptionPane.INFORMATION_MESSAGE);
     }
    if(ML.succ==2&&flag==0&&pn==2)
    {
        flag=1;
        t.stop=true;
        JOptionPane.showMessageDialog(this,"Congratulations, you won!!","Game Won",JOptionPane.INFORMATION_MESSAGE);
     }
    if(ML.succ==1&&flag==0&&pn==2)
    {
        flag=1;
        t.stop=true;
        JOptionPane.showMessageDialog(this,"Sorry, you lost!!","Game Lost",JOptionPane.ERROR_MESSAGE);
     }
    if(ML.succ==2&&flag==0&&pn==1)
    {
        flag=1;
        t.stop=true;
        JOptionPane.showMessageDialog(this,"Sorry, you lost!!","Game Lost",JOptionPane.ERROR_MESSAGE);
     }
    if(fail&&flag==0)
    {
        flag=1;
        t.stop=true;
        if(enabled_flag)
            JOptionPane.showMessageDialog(this,"Congratulations, you won!!","Game Won",JOptionPane.INFORMATION_MESSAGE);
        else
             JOptionPane.showMessageDialog(this,"Sorry, you lost!!","Game Lost",JOptionPane.ERROR_MESSAGE);
    }
}

    @Override
    public void actionPerformed(ActionEvent e)
    {
      t.stop=false;
      if(e.getSource()==new_game)
      {
          fail=false;
          flag=0;
          t.setTime(0);
          t.stop=true;
          t.ltime.setText("0:0:0");
          ML.new_game();
       }
      else{
      for(int i=0;i<10;i++) {
          for(int j=0;j<10;j++) {
                  if(e.getSource() == squares[i][j])
                  {
                        try {
                            Open(i, j);
                            osToServer.writeInt(i);
                            osToServer.writeInt(j);
                            osToServer.writeInt(left);
                            osToServer.flush();
                            enabled_flag=false;
                        } catch (IOException ex) {
                            Logger.getLogger(GameGridGui.class.getName()).log(Level.SEVERE, null, ex);
                        }

              }
            }
        }
      }

    buildButtons();
  }
 public void Open(int i, int j)
 {
     if(ML.get_isFlag(i, j)==0)
     {
                        if(ML.get_label(i, j)==-1)
                        {
                            fail=true;
                        }
                        else
                        {
                            ML.openCell(i, j);
                            ML.issucc();
                        }
     }
 }

    @Override
  public void mouseClicked(MouseEvent e)
{
 if(enabled_flag)
 {
       for(int i=0;i<10;i++)
       {
                for(int j=0;j<10;j++)
                {
                  if(e.getSource()==squares[i][j])
                  {
                        try {
                            ML.set_isFlag(i,j,pn);
                            ML.issucc();
                             osToServer.writeInt(i);
                                  osToServer.writeInt(j);
                                  osToServer.writeInt(right);
                                  osToServer.flush();
                        } catch (IOException ex) {
                            Logger.getLogger(GameGridGui.class.getName()).log(Level.SEVERE, null, ex);
                        }
                  }

                  if(e.getSource()==labels[i][j])
                  {
                        if((e.getModifiers()& InputEvent.BUTTON1_MASK)==InputEvent.BUTTON1_MASK)
                        {

                             if(i>0&&j>0)
                             {
                                 Open(i-1,j-1);
                             }
                             if(i>0)
                             {
                                 Open(i-1,j);
                             }
                             if(i>0&&j<9)
                             {
                                 Open(i-1,j+1);
                             }
                             if(j>0)
                             {
                                 Open(i,j-1);
                             }
                             if(j<9)
                             {
                                 Open(i,j+1);
                             }
                             if(i<9&&j>0)
                             {
                                 Open(i+1,j-1);
                             }
                             if(i<9)
                             {
                                 Open(i+1,j);
                             }
                             if(i<9&&j<9)
                             {
                                 Open(i+1,j+1);
                             }
                        }

                    }
            }
        }
    enabled_flag=false;
    buildButtons();
 }

}
 public static void main(String[] args)
 {
  GameGridGui g = new GameGridGui();
  JFrame frame = new JFrame("Minesweeper");
  frame.add(g);
  frame.setSize(400,400);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  frame.setVisible(true);

 }

    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}


    class HandleAClient extends Thread {
    private Socket connectToServer; // A connected socket

    /** Construct a thread */
    public HandleAClient(Socket socket) {
      connectToServer = socket;
    }
    public void run() {
            try {
                int i;
                int j;
                int lr;
                int t;
                if(pn==1)
                    t=2;
                else
                    t=1;

                // Create data input and output streams
                DataInputStream isFromServer = new DataInputStream(connectToServer.getInputStream());
                while(true)
                {
                     i=isFromServer.readInt();
                     j=isFromServer.readInt();
                     lr=isFromServer.readInt();
                     if(lr==left)
                         Open(i,j);
                     else if(lr==right)
                     {
                         ML.set_isFlag(i,j,t);
                         ML.issucc();
                     }
                     enabled_flag=true;
                     buildButtons();

                }

            } catch (IOException ex) {
                Logger.getLogger(GameGridGui.class.getName()).log(Level.SEVERE, null, ex);
            }

      }

}
}

