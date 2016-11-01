package tesina2012.utils;

/**
 * Semplice classe per la gestione degli errori.
 * @author Andrea Valenti
 * @version 1.0
 */
public class ErrorHandler {
    
    /**
     * Messaggio d'errore.
     * @since 1.0
     */
    private static String error="";
    
    /**
     * Getter di 'error'.
     * @return Ultimo errore ricordato.
     * @since 1.0
     */
    public static String getError(){
        return error;
    }
    
    /**
     * Salva il messaggio dell'eccezione 'ex'.
     * @param ex - eccezione da cui salvare l'errore.
     * @since 1.0
     */
    public static void remember(Exception ex){
        error=ex.getMessage();
    }
}
