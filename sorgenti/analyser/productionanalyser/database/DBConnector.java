
package productionanalyser.database;

/**
 * Una classe che esegue connesioni a un db 
 * deve implementare questa interfaccia.
 * @author Andrea Valenti
 * @version 1.0
 */
public interface DBConnector {
    /**
     * @since 1.0
     * Tenta una connessione al database.
     * @return esito del tentativo di connessione
     */
    public boolean connect();
    
    /**
     * @since 1.0
     * Esegue una query di tipo SELECT sul database a cui 
     * si e' connessi.
     * @param query - query da eseguire
     * @return risultato della query
     */
    public QueryResult query(String query);
    
    /**
     * @since 1.0
     * Esegue una operazione di modifica del database.
     * Con questo metodo e' possibile eseguire tutte le query
     * di modifica del database (UPDATE,INSERT,DELETE,CREATE,DROP,...)
     * @param query - query da eseguire
     * @return esito della query
     */
    public boolean update(String query);
    
    /**
     * @since 1.0
     * Si disconnette dal database attualmente connesso.
     */
    public void disconnect();
}
