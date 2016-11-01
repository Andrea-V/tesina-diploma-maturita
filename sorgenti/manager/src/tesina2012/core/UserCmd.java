
package tesina2012.core;

import java.util.concurrent.LinkedBlockingQueue;
import tesina2012.socket.DataListener;
import java.util.concurrent.CopyOnWriteArrayList;
import tesina2012.database.QueryResult;
import tesina2012.database.MySQLdb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.err;
import static java.lang.System.out;

/**
 *
 * @author andrea
 */
public class UserCmd extends Thread{
    private boolean stopp;
    private CopyOnWriteArrayList<Thread> act_conns;
    private LinkedBlockingQueue msg_queue;
    
    public UserCmd(CopyOnWriteArrayList<Thread> act_conns,LinkedBlockingQueue msg_queue){
        stopp=false;
        this.act_conns=act_conns;
        this.msg_queue=msg_queue;
    }
    
    @Override
    public void run(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        out.print("Welcome to our production flow control application.\nType here your command: ");
        while(!stopp)
            try {
                out.print("\n>> ");
                executeCmd(parseCmd(br.readLine()));
            } catch (IOException ex) {
                err.println("Error: I/O."+ex.getMessage());
            }

    }
    
    public void setStop(boolean stop){
        this.stopp=stop;
    }
    
    private String[] parseCmd(String cmd){
        String[] res=cmd.split(" ");
        
        if(res[0].equals("add")||res[0].equals("delete")||res[0].equals("list")||res[0].equals("help"))
            return res;
        else 
            return null;
    }
    
    private void executeCmd(String[]cmd){
        try{
            if(cmd[0].equals("add"))
                ex_add(cmd[1],Integer.parseInt(cmd[2]));
            else if(cmd[0].equals("delete"))
                ex_delete(Integer.parseInt(cmd[1]));
            else if(cmd[0].equals("list"))
                ex_list();
            else if(cmd[0].equals("help"))
                ex_help();
        }catch(NullPointerException npex){
            err.println("Error: command not found. Type 'help' to display the command list.");
        }catch(Exception aiex){
            err.println("Error: bad command options. Type 'help' to display the command list.");
        }
        
    }
    
    private void ex_help(){
        out.println("Command list:");
        out.println("add xx yy - add a new connection in the connection table.");
        out.print("\t xx - ip adress");
        out.println("\n\t yy - port number");
        out.println("delete xx - delete a connection in the connection table.");
        out.println("\txx - connection ID");
        out.println("list - display the current available connections.");
        out.println("help - display the command list.");
    }
    
    private void ex_list(){
        MySQLdb db=new MySQLdb("production", "root", "");
        QueryResult qr;
        db.connect();
        qr=db.query("SELECT * FROM available_connections");
        out.println("\n"+qr);
        db.disconnect();
    }
    
    private void ex_delete(int id){
        MySQLdb db=new MySQLdb("production", "root", "");
        QueryResult qr;
        db.connect();
        
        act_conns.remove(id);
        
        db.update("DELETE FROM available_connections WHERE ID = "+id);
        db.disconnect();
    }
    
    private void ex_add(String ip,int port){
        MySQLdb db=new MySQLdb("production", "root", "");
        QueryResult qr;
        DataListener dl=new DataListener(ip,port,msg_queue);
        db.connect();
        
        act_conns.add(dl);
        dl.start();
        
        db.update("INSERT INTO available_connections VALUES(NULL,'"+ip+"',"+port+")");
        db.disconnect();
    }
    
}
