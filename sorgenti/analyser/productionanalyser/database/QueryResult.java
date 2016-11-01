
package productionanalyser.database;

import tesina2012.utils.ErrorHandler;
import java.sql.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Riceve il risultato di una query di tipo SELECT.
 * Estrae la tabella di risultato e altre informazioni notevoli.
 * @author Andrea Valenti
 * @version 1.1
 * @see ErrorHandler
 */
public class QueryResult{
    /**
     * @since 1.0
     * Risultato della query rappresentato come lista di Record.
     */
    private CopyOnWriteArrayList<Record> result;
    
    /**
     * @since 1.1
     * Nomi dei campi della tabella di risultato.
     */
    private Record columns_name;
    
    /**
     * @since 1.0
     * Numero di campi della tabella di risultato.
     */
    private int columns;
    
    /**
     * @since 1.0
     * Inizializza i valori dei campi partendo da un ResultSet.
     * @param rs - ResultSet di base.
     * @see ResultSet
     */
    public QueryResult(ResultSet rs){
        try {
            result=new CopyOnWriteArrayList<Record>();
            this.columns=rs.getMetaData().getColumnCount();
            this.extractInformation(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
            ErrorHandler.remember(ex);
        }
    }
    
    /**
     * @since 1.0
     * Estrae le informazioni utili di un ResultSet.
     * @param rs - ResultSet di base.
     * @see ResultSet
     */
    private void extractInformation(ResultSet rs){
        extractResultTable(rs);
        extractColumnsName(rs);
        try {
            rs.close();
        } catch (SQLException ex) {
           ex.printStackTrace();
           ErrorHandler.remember(ex);
        }
    }
    
    /**
     * @since 1.1
     * Estrae i nomi dei campi della tabella di risultato.
     * Il risultato viene salvato in 'columns_name'.
     * @param rs - ResultSet di base.
     * @see ResultSet
     */
    private void extractColumnsName(ResultSet rs){
        try {
            ResultSetMetaData rsmd= rs.getMetaData();
            columns_name=new Record();
            for(int i=0;i<columns;i++)
                columns_name.addValue(rsmd.getColumnName(i+1));
        } catch (SQLException ex) {
            ex.printStackTrace();
             ErrorHandler.remember(ex);
        }
            
    }
    
    /**
     * @since 1.0
     * Estrae il risultato della query da un ResultSet.
     * Il risultato viene salvato in 'result'. 
     * @param rs - ResultSet di base
     * @see ResultSet
     */
    private void extractResultTable(ResultSet rs){
        Record rcd;
        try {    
            while(rs.next()){
                rcd = new Record();
                for (int i=0; i<columns; i++)       //aggiungo i valori al record
                    rcd.addValue(rs.getString(i+1));
                result.add(rcd);                    //aggiungo il record alla lista
             }
        } catch (SQLException ex) {
            ex.printStackTrace();
            ErrorHandler.remember(ex);
        }
    }
    
    /**
     * @since 1.0
     * Ritorna la tabella di risultato.
     * @return CopyOnWriteArrayList contenete i record della tabella.
     * @see CopyOnWriteArrayList
     */
    public CopyOnWriteArrayList<Record> getResultTable(){
        return result;
    }
    
    /**
     * @since 1.1
     * Ritorna il numero di campi della tabella di risultato.
     * @return numero di campi di 'result'.
     */
    public Record getColumnsName(){
        return columns_name;
    }
    
    /**
     * @since 1.0
     * Converte l'oggetto in forma stampabile.
     * @return stringa di rappresentazione dell'oggetto.
     */
    @Override
    public String toString(){
        String str="";
        str+=(columns_name.toString()+"\n");
        for(int i=0;i<columns;i++)
            str+="----------------";
        str+="\n";
        for(Record rcd:result)
            str+=(rcd.toString()+"\n");
        return str;
    }
}
