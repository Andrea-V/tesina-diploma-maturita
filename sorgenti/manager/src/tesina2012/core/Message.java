package tesina2012.core;

import static java.lang.System.err;
import java.util.Date;
/**
 *
 * @author andrea
 */
public class Message {
    private int DEF_PKT_LENGHT=4;
    private int mac_id;
    private int op_id;
    private int com_id;
    private State state;
    private Date rec_d;
    
    public Message(byte[]msg){
        if(msg.length>DEF_PKT_LENGHT){
            err.println("Errore: la stringa non e' un messaggio valido.");
            System.exit(1);
        }
        rec_d=new Date();
        mac_id=msg[0];
        op_id =msg[1];
        com_id=msg[2];
        
        switch(msg[3]){//DA AGGIORNARE!!!
            case 1:
              state=State.WORKING;
            break;
            case 2:
                state=State.WAITING;
            break;
            case 3:
                state=State.ERROR;
            break;
            default:
                err.println("Errore!!! stato non previsto");
        }
    }
    
    public Message(int mac_id,int op_id,int com_id,State state){
        rec_d=new Date();
        this.mac_id=mac_id;
        this.op_id =op_id;
        this.com_id=com_id;
        this.state =state;
    }
    
    public int getMacId(){
        return mac_id;
    }
    public int getOpId(){
        return op_id;
    }
    public int getComId(){
        return com_id;
    }
    public State getState(){
        return state;
    }
    public Date getDateOfArrival(){
        return rec_d;
    }
    
    @Override
    public String toString(){
        String str="\n";
        str+=rec_d+"\n";
        str+="MAC_ID\tOP_ID\tCOM_ID\tSTATE\n";
        str+=mac_id+"\t"+op_id+"\t"+com_id+"\t"+state+"\n";
        return str;
    }
}
