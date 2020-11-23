package MinesweeperMultiPlayer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.TimerTask;
import javax.swing.JLabel;

/**
 *
 * @author Bassam
 */
public class  MyTimer extends TimerTask
{
private int times;
public JLabel ltime;
public boolean stop=true;
public MyTimer()
{
    times=0;
    ltime =new JLabel("0:0:0");
    ltime.setHorizontalAlignment(0);
}
public String getTime(){
    int h=0;
    int m=0;
    int s=0;
    int temp;
    h=times/3600;
    temp=times%3600;
    m=temp/60;
    s=temp%60;

    return ""+h+":"+m+":"+s;
}
public void setTime(int t){
    times=t;
}
    @Override
    public void run()
    {
        if(!stop)
        {
            times++;
            ltime.setText(getTime());
            ltime.setHorizontalAlignment(0);
        }
    }
}
