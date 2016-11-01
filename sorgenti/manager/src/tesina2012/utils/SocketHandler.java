package tesina2012.utils;

import java.io.*;
import java.net.*;
import static java.lang.System.err;

/**
 * Fornisce alcune funzionalita' utili per agire sui socket.
 * @author Andrea Valenti
 * @version 1.1
 * @see ErrorHandler
 */
public class SocketHandler{
    
    /**
     * @since 1.0
     * Abilita l'uso dello stream di input di un socket.
     * @param skt - socket da cui estrarre l'input stream
     * @return Reader dell'input stream di 'skt'
     */
    public static DataInputStream allowInputOn(Socket skt) {
        DataInputStream dis=null;
        try {
            dis=new DataInputStream(skt.getInputStream());
            
        } catch (IOException ex) {
            ex.printStackTrace();
            err.println("ERRORE: "+ex.getMessage());
        }
        return dis;
    }
    
    /**
     * Abilita l'uso dello stream di output di un socket.
     * @param skt - socket da cui estrarre l'output stream
     * @return Writer dell'output stream di 'skt'
     * @since 1.0
     */
    public static DataOutputStream allowOutputOn(Socket skt) {
        DataOutputStream dos = null;
        try {
            dos=new DataOutputStream(skt.getOutputStream());
            
        } catch (IOException ex) {
            ex.printStackTrace();
            err.println("ERRORE: "+ex.getMessage());
        }
        return dos;
    }
}
