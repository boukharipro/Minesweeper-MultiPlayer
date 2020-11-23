/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MinesweeperMultiPlayer;
import java.io.Serializable;

public class GameLogic implements Serializable
{

    public int succ;
    int nu_mines=5;
    int number_flags1=nu_mines;
    int number_flags2=nu_mines;
    int [][] labels=new int[10][10];
    boolean [][] isopen=new boolean[10][10];
    int [][] isFlag=new int[10][10];
    public GameLogic()
    {
        succ=0;
        number_flags1=nu_mines;
        number_flags2=nu_mines;
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                labels[i][j]=0;
                isopen[i][j]=false;
                isFlag[i][j]=0;
            }
        }
        int mines=nu_mines;
        while(mines>0)
        {
            int x=(int) Math.floor ( Math.random ( ) * 10 );
            int y=(int) Math.floor ( Math.random ( )* 10 );
            if(labels[x][y] != -1)
            {
                labels[x][y] = -1;
                mines--;
            }

        }

    }
    public void new_game()
    {
        succ=0;
        number_flags1=nu_mines;
        number_flags2=nu_mines;
        for(int i=0;i<10;i++)
        {
            for(int j=0;j<10;j++)
            {
                labels[i][j]=0;
                isopen[i][j]=false;
                isFlag[i][j]=0;
            }
        }
        int mines=nu_mines;
        while(mines>0)
        {
            int x=(int) Math.floor ( Math.random ( ) * 10 );
            int y=(int) Math.floor ( Math.random ( )* 10 );
            if(labels[x][y] != -1)
            {
                labels[x][y] = -1;
                mines--;
            }

        }
    }
    public int get_label(int i, int j)
    {
        return labels[i][j];
    }
    public int get_flags(int pn)
    {
        if(pn==1)
          return number_flags1;
        if(pn==2)
         return number_flags2;
        else
            return 0;
    }
    public boolean get_isopen(int i, int j)
    {
        return isopen[i][j];
    }
    public void set_isopen(int i, int j)
    {
        isopen[i][j]=true;
    }
    public int get_isFlag(int i, int j)
    {
            return isFlag[i][j];
    }
    public void set_isFlag(int i, int j,int pn)
    {
        if(pn==1)
        {
            if(isFlag[i][j]==0&&number_flags1>0) {
                isFlag[i][j]=1;
                number_flags1--;
            }
            else if(isFlag[i][j]==1) {
                isFlag[i][j]=0;
                number_flags1++;
            }
        }
        if(pn==2)
        {
            if(isFlag[i][j]==0&&number_flags2>0) {
                isFlag[i][j]=2;
                number_flags2--;
            }
            else if(isFlag[i][j]==2) {
                isFlag[i][j]=0;
                number_flags2++;
            }
        }
    }
    public void issucc()
    {
        int flag=0;
        int flag1_error=0;
        int flag1_succ=0;
        int flag2_error=0;
        int flag2_succ=0;
        for(int i=0;i<10;i++)
          for(int j=0;j<10;j++)
              if(!isopen[i][j]&&isFlag[i][j]==0)
              {
                  flag=1;
                  break;
              }
        if(flag==0)
        {
            for(int i=0;i<10;i++)
                for(int j=0;j<10;j++)
                {
                    if(isFlag[i][j]==1)
                    {
                        if(labels[i][j]==-1)
                            flag1_succ++;
                        else
                            flag1_error++;
                    }
                    else if(isFlag[i][j]==2)
                    {
                        if(labels[i][j]==-1)
                            flag2_succ++;
                        else
                            flag2_error++;
                    }
                }
            if(flag1_succ>flag2_succ)
                succ=1;
            else if(flag1_succ<flag2_succ)
                succ=2;
            else if(flag1_error<flag2_error)
                succ=1;
            else if(flag1_error>flag2_error)
                succ=2;
            else
                succ=1;
        }
    }
    public boolean openCell(int i, int j)
    {
        if(i<0||i>9||j<0||j>9||isFlag[i][j]!=0)
            return false;
        if(isopen[i][j])
            return true;
        isopen[i][j]=true;
        if(i>0)
            if(labels[i-1][j]==-1)
                labels[i][j]++;
        if(i>0&&j>0)
            if(labels[i-1][j-1]==-1)
                labels[i][j]++;
        if(j>0)
            if(labels[i][j-1]==-1)
                labels[i][j]++;
        if(i<9&&j>0)
            if(labels[i+1][j-1]==-1)
                labels[i][j]++;
        if(i<9)
            if(labels[i+1][j]==-1)
                labels[i][j]++;
        if(i<9&&j<9)
            if(labels[i+1][j+1]==-1)
                labels[i][j]++;
        if(j<9)
            if(labels[i][j+1]==-1)
                labels[i][j]++;
        if(i>0&&j<9)
        if(labels[i-1][j+1]==-1)
            labels[i][j]++;

       if(labels[i][j]==0)
       {
            openCell( i - 1, j );
            openCell( i - 1, j - 1 );
            openCell( i, j - 1 );
            openCell( i + 1, j - 1 );
            openCell( i + 1, j );
            openCell( i + 1, j + 1 );
            openCell( i , j + 1 );
            openCell( i - 1, j + 1 );
       }
        return true;

     }


}
