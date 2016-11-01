package tesina2012.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import tesina2012.database.*;
import tesina2012.socket.DataListener;
import tesina2012.utils.ErrorHandler;
import static java.lang.System.err;
import static java.lang.System.out;

/**
 *
 * @author andrea
 */
public class ConnectionManager {

    private CopyOnWriteArrayList<Record> ava_conns;  //tabella delle connessioni disponibili
    private CopyOnWriteArrayList<Thread> act_conns;  //tabella delle connessioni attive
    private LinkedBlockingQueue msg_queue;
    private boolean stop;
    
    public ConnectionManager() {
        stop=false;
        msg_queue = new LinkedBlockingQueue<String>();
        act_conns=new CopyOnWriteArrayList<Thread>();
    }
    
    public void stopManaging(){
        stop=true;
    }
    
    public void updateConnsTable() {
        MySQLdb db = new MySQLdb("production", "root", "");
        QueryResult qr;
        String q1 = "SELECT * FROM available_connections";

        if(!db.connect())
            err.println(ErrorHandler.getError());
        if((qr=db.query(q1))==null)
            err.println(ErrorHandler.getError());

        ava_conns = qr.getResultTable();
    }
    
    public void startConnections() {
        String ip_addr;
        int port, ts = 0;
        for (Record rcd : ava_conns) {
            //ottengo i dati per la connessione
            ip_addr = rcd.getValueAt(1);
            port = Integer.parseInt(rcd.getValueAt(2));
            
            //creo i listener su thread separati
                act_conns.add(new DataListener(ip_addr,port,msg_queue));
                act_conns.get(ts).start();
                ts++;
            
        }
    }

    public void startManaging() {
        Message msg=null;
        MessageExecutor mex=null;
        
        while (!stop) {
            try {
                msg = (Message) msg_queue.take();
            } catch (InterruptedException ex) {
                ErrorHandler.remember(ex);
            }

            mex=new MessageExecutor(msg);
            mex.start();
        }
        stop=false;
    }

    public void createUserThread() {
        UserCmd cmd=new UserCmd(act_conns,msg_queue);
        cmd.start(); 
    }
}
