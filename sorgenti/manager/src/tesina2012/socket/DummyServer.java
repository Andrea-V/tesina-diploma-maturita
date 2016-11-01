package tesina2012.socket;

import java.util.logging.Level;
import java.util.logging.Logger;
import tesina2012.utils.*;
import java.io.*;
import java.net.*;
import static java.lang.System.out;

/**
 * Simula il comportamento della scheda di conversione RS232/TCP-IP (M)
 * Invia pacchetti al client connesso a tempi regolari.
 * @author andrea
 * @version 0.1
 */
public class DummyServer extends Thread {

    private int port;
    private int nconn = 0;
    private ServerSocket server;

    public DummyServer(int port) {
        this.port = port;
    }

    public boolean init() {
        try {
            server = new ServerSocket(port);
        } catch (IOException ex) {
            ErrorHandler.remember(ex);
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        init();
        Socket s1 = null;
        //BufferedWriter bw=null;
        DataOutputStream dos = null;
        while (true) {
            try {
                // Il server resta in attesa di una richiesta
                //out.println("Waiting for requests...");
                s1 = server.accept();
                out.println("Connesso a 192.168.4.127\n");
                nconn++;

                // Ricava lo stream di output associate al socket
                dos = SocketHandler.allowOutputOn(s1);
                
                for(int i=0;i<2;i++){
                    dos.writeByte(1);
                    dos.writeByte(2);
                    dos.writeByte(1);
                    dos.writeByte(2);
                    delay(1000);
                    dos.writeByte(1);
                    dos.writeByte(2);
                    dos.writeByte(1);
                    dos.writeByte(1);
                    delay(1000);
                }
                dos.writeByte(1);
                dos.writeByte(2);
                dos.writeByte(1);
                dos.writeByte(2);
                delay(1000);    
               
                
                dos.close();
                s1.close();
                //out.println("Chiusura connessione effettuata\n");

            } catch (IOException ex) {
                ex.printStackTrace();
                ErrorHandler.remember(ex);
                return;
            }
        }
    }

    private void delay(long time) {
        try {
            this.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(DummyServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
