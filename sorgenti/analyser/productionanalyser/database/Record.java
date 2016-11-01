package productionanalyser.database;

import java.util.concurrent.CopyOnWriteArrayList;
/**
 * Rappresenta un singolo record di database.
 * @author Andrea Valenti
 * @version 1.0
 */
public class Record {
    /**
     * @since 1.0
     * Lista di tutti i valori del record.
     */
    CopyOnWriteArrayList<String> values;
    
    /**
     * @since 1.0;
     * Numero di campi del record.
     */
    int columns;
    
    /**
     * @since 1.0
     * Inizializza il record.
     */
    public Record(){
        this.columns=0;
        this.values=new CopyOnWriteArrayList<String>();
    }
    
    /**
     * @since 1.0
     * Inizializza il record con gli elementi di 'list'.
     * @param list - lista da cui estrarre gli elementi.
     */
    public Record(CopyOnWriteArrayList<String>list){
        columns=list.size();
        for(int i=0;i<columns;i++)
            this.values.add(list.get(i));
    }
    
    /**
     * @since 1.0
     * Imposta il valore degli elementi del record.
     * @param list - lista dei nuovi valori del record.
     */
    public void setValues(CopyOnWriteArrayList<String> list){
        columns=list.size();
        for(int i=0;i<columns;i++)
            this.values.add(list.get(i));
    }
    
    /**
     * @since 1.0
     * Aggiunge un nuovo campo al record. 
     * Il campo 'columns' viene aggiornato di conseguenza.
     * @param new_value - nuovo campo da inserire.
     */
    public void addValue(String new_value){
        columns++;
        values.add(new_value);
    }
    
    /**
     * @since 1.0
     * Ritorna i valori presenti nei campi del record.
     * @return CopyOnWriteArrayList contenente i valori dei campi del record.
     * @see CopyOnWriteArrayList
     */
    public CopyOnWriteArrayList<String> getValues(){
        return this.values;
    }
    
    /**
     * @since 1.0
     * Ritorna i campo i-esimo del record.
     * @param i - indice del campo da ritornare.
     * @return i-esimo campo del record.
     */
    public String getValueAt(int i){
        return this.values.get(i);
    }
    
    /**
     * @since 1.0
     * Getter di 'columns'.
     * @return numero di colonne del record.
     */
    public int getColumnsCount(){
        return this.columns;
    }
    
    /**
     * @since 1.0
     * Converte l'oggetto in forma stampabile.
     * @return stringa di rappresentazione dell'oggetto.
     */
    @Override
    public String toString(){
        String str="";
        String concat="";
        for(int i=0;i<values.size();i++){
            concat=(values.get(i).length()>8)?(" \t"):("\t\t");
            str+=(values.get(i)+concat);
        }
        return str;
    }
}

