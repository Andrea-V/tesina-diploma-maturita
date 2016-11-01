package productionanalyser.database;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import tesina2012.utils.ErrorHandler;
import java.sql.*;
import static java.lang.System.err;

/**
 * Classe dedicata alla gestione di un database MySQL.
 * Gestisce l'apertura e la chiusura della connessione col Database.
 * Fornisce i metodi per l'esecuzione delle query sul Database.
 * Implementa l'interfaccia DBconnector.
 * @author Andrea Valenti
 * @version 1.3
 * @see DBconnector, ErrorHandler
 */        
public class MySQLdb implements DBConnector {
   
    //semaforo con un solo permesso, il db puo' essere usato solo un thread alla volta
    Semaphore semaphore=new Semaphore(1);
    
    /**
    * @since 1.0
    * Nome del Database a cui connettersi.
    */
   private String db_name;
   
   /**
    * @since 1.0
    * Nome utente utilizzato per la connessione al Database.
    */
   private String username;
   
   /**
    * @since 1.0
    * Password usata per la connessione al Database.
    */
   private String password;
   
   /**
    * @since 1.0
    * La connessione col Database.
    */
   private Connection db;
   
   /**
    * @since 1.0
    * Flag che indica se la connessione è attiva o meno.
    */
   private boolean connected;

   /**
    * @since 1.0
    * Inizializza i campi prima della connesione.
    * @param db_name - nome del database.
    */
   public MySQLdb(String db_name){ 
       this(db_name, "", "");
   }
   
   /**
    * @since 1.0
    * Inizializza i campi prima della connesione.
    * @param db_name - nome del database.
    * @param username - nome utente con cui si sta accedendo
    * @param password - password associata
    */
   public MySQLdb(String db_name, String username, String password) {
      this.db_name = db_name;
      this.username = username;
      this.password = password;
      connected = false;
   }
   public boolean acquireRights(){
       try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
           ErrorHandler.remember(ex);
           return false;
        }
       return true;
   }
   public void releaseRights(){
       semaphore.release();
   }
   /**
    * @since 1.0
    * Apre la connessione con il Database.
    * Ereditato dall'interfaccia DBconnector
    * @return esito del tentativo di connessione.
    * @see DBconnector
    */
    @Override
   public boolean connect() {
         connected = false;
     if(!acquireRights())
         return false;
      try {
         Class.forName("com.mysql.jdbc.Driver");// Carico il driver JDBC per la connessione con il database MySQL
         if (!db_name.equals("")) {
            if (username.equals(""))
               db = DriverManager.getConnection("jdbc:mysql://localhost/" + db_name);// La connessione non richiede nome utente e password
            else
               if (password.equals(""))
                  db = DriverManager.getConnection("jdbc:mysql://localhost/" + db_name + "?user=" + username);// La connessione non necessita di password
               else
                  db = DriverManager.getConnection("jdbc:mysql://localhost/" + db_name + "?user=" + username + "&password=" + password);// La connessione necessita della password
            connected = true;
         } else {
            err.println("Manca il nome del database!!");
            releaseRights();
            System.exit(0);
         }
      } catch (Exception e){
  
          ErrorHandler.remember(e);
      }
      if(connected==false)
          releaseRights();
      return connected;
   }

   /**
     * @since 1.1
     * Esegue una query di selezione dati sul Database.
     * @param query - query da eseguire.
     * @return risultato della query.
     * @see DBconnector
     */
    @Override
   public QueryResult query(String query) {  
        QueryResult qres=null;
        try {
         Statement stmt = db.createStatement();     // Creo lo Statement per l'esecuzione della query
         qres=new QueryResult(stmt.executeQuery(query));   // Ottengo il ResultSet dell'esecuzione della query
         stmt.close();
      } catch (Exception e) { 
          e.printStackTrace();
          ErrorHandler.remember(e);
      }
      return qres;
   }

   /**
     * @since 1.2
     * Esegue una query di aggiornamento sul Database.
     * @param query - query da eseguire.
     * @return TRUE se l'esecuzione è adata a buon fine, FALSE se c'è stata un'eccezione.
     */
    @Override
   public boolean update(String query) { 
      int num = 0;
      boolean result = false;
      try {
         Statement stmt = db.createStatement();
         num = stmt.executeUpdate(query);
         result = true;
         stmt.close();
      } catch (Exception e) {
         e.printStackTrace();
         ErrorHandler.remember(e);
         result = false;
      }
      return result;
   }
    
    /**
     * @since 1.0
     * Si disconnette dal database.
     * @see DBconnector
     */
    @Override
   public void disconnect() {
      try {
         releaseRights();
         db.close();
         connected = false;
      } catch (Exception e) { 
          e.printStackTrace(); 
      }
   }
    
   /**
     * @since 1.1
     * Getter di 'connected'.
     * @return TRUE se al momento si e' connessi a un db, FALSE se si e' disconnessi.
     */
   public boolean isConnected() { 
       return connected; 
   }
}