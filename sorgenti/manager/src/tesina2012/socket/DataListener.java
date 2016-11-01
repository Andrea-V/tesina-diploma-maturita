package tesina2012.socket;


import java.io.DataInputStream;
import java.net.*;
import static java.lang.System.err;
import static java.lang.System.out;
import java.util.concurrent.LinkedBlockingQueue;
import tesina2012.core.Message;
import tesina2012.utils.*;

/**
 * Si connette con un server e rimane in ascolto di pacchetti.
 * Puo' essere eseguita in modo asincrono.
 * @author andrea
 * @version 1.1
 * @see SocketHandler, Runnable
 */
public class DataListener extends Thread {
    
    private int DEF_PKT_LEN=4;
    
    private LinkedBlockingQueue msg_queue;
    /**
     * Socket client in ascolto.
     * @since 1.0
     */
    private Socket c_skt;
    /**
     * Indirizzo ip del server.
     * @since 1.0
     */
    private String ip_address;
    /**
     * Porta del server.
     * since 1.0
     */
    private int port;
    /**
     * Provoca l'uscita dal metodo run()
     * @since 1.0
     */
    private boolean stop;

    /**
     * Crea la connessione con il server.
     * @param ip_address - indirizzo ip del server.
     * @param port - porta del server.
     * @since 1.0
     */
    public DataListener(String ip_address, int port, LinkedBlockingQueue msg_queue) {
        this.msg_queue = msg_queue;
        this.stop = false;
        this.ip_address = ip_address;
        this.port = port;
        try {
            c_skt = new Socket(ip_address, port);
        } catch (Exception ex) {
            err.println(ex.getMessage());
        }
    }

    /**
     * Provoca lo stop del metodo run().
     * Se invocato asincronamente come thread, 
     * provoca l'uscita dal thread.
     * @since 1.0
     */
    public void exit() {
        stop = true;
    }

    /**
     * Comincia l'ascolto di dati in arrivo.
     * @since 1.0
     * @see Runnable
     */
    @Override
    public void run() {
        byte[] pkt=null;
        DataInputStream dis=SocketHandler.allowInputOn(c_skt);
        Message msg;
        
        while (!stop) {
            try {
                
                if (dis.available()>=DEF_PKT_LEN) {

                    pkt=new byte[DEF_PKT_LEN];
                    dis.readFully(pkt);
                    msg = new Message(pkt);
                    msg_queue.put(msg);
                }
                
            } catch (Exception ex) {
                err.print(ex.getMessage());
            }
            delay(1);
        }
    }
    /**
     * Getter di 'ip_address'
     * @return indirizzo ip a cui si e' connessi.
     * @since 1.0
     */
    public String getIpAddress() {
        return ip_address;
    }

    /**
     * Getter di 'port'
     * @return porta a cui si e' connessi.
     * @since 1.0
     */
    public int getPort() {
        return port;
    }
    private void delay(long millis){
        try {
            sleep(millis);
        } catch (InterruptedException ex) {
            ErrorHandler.remember(ex);
        }
    }
    
    @Override
    public String toString(){
        String str="Stato connessione: \n";
        str+="Thread corrente: "+super.toString()+"\n";
        str+="IP: "+this.getIpAddress()+"\n";
        str+="PORT: "+this.getPort()+"\n";
        return str;
    }
}
