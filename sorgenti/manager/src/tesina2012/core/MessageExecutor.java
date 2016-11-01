
package tesina2012.core;


import static java.lang.System.err;
import java.sql.Timestamp;
import tesina2012.database.DBConnector;
import tesina2012.database.MySQLdb;
import tesina2012.utils.ErrorHandler;

/**
 * 
 * @author andrea
 */
public class MessageExecutor extends Thread{
    
    Message msg;
    
    public MessageExecutor(Message msg){
        this.msg=msg;
    }
    
    private void dbUpdate(DBConnector dbc){
        Timestamp ts=new Timestamp(msg.getDateOfArrival().getTime());
        String query="INSERT INTO daily_log VALUES("+msg.getMacId()+","+msg.getOpId()+","+msg.getComId()+",'"+msg.getState()+"',TIMESTAMP('"+ts.toString()+"'))";
        if(!dbc.connect())err.println(ErrorHandler.getError());
        
        if(!dbc.update(query))err.println(ErrorHandler.getError());
        dbc.disconnect();
    }
    
    @Override
    public void run() {
        dbUpdate(new MySQLdb("production","root",""));
    }

}
