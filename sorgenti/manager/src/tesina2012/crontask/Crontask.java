package tesina2012.crontask;

import java.util.concurrent.CopyOnWriteArrayList;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import tesina2012.database.Record;
import tesina2012.database.MySQLdb;
import tesina2012.database.QueryResult;
import tesina2012.utils.DateHandler;
import static java.lang.System.out;

/**
 *
 * @author andrea
 */
public class Crontask {

    public static void daily(String date) {
        ArrayList<Integer> ial;
        QueryResult qr, qi;
        String q_slct_all = "SELECT * FROM daily_log";
        String q_create = "CREATE table " + date + " ("
                + "machine_id INTEGER(255) NOT NULL,"
                + "pieces INTEGER(255) NOT NULL,"
                + "total_time INTEGER(255) NOT NULL,"
                + "working_time INTEGER(255) NOT NULL)";
        String q_insert="INSERT INTO "+date+" ";
        MySQLdb db = new MySQLdb("production", "root", "");
        db.connect();

        //creo la teblla del giorno e seleziono tutto il daily log
        if(!db.update(q_create))out.println("problema update");
        if(null == (qr = db.query(q_slct_all)))out.println("Problema slct all");
        
        
        //trovo tutti gli id delle macchine
        ial=seekID(qr);
        int p;
        
        //per ogni macchina conto i pezzi fatti, il tempo utile, il tempo totale e salvo nella tabella del giorno
        for(Integer i:ial){
            if(null==(qi=db.query(q_slct_all+" WHERE fk_machine = "+i+" ORDER BY date_of_arrival")))out.println("Problema slct " + i);
            p=countPieces(qi);
            db.update(q_insert+"VALUES("+i+","+p+","+totalTime(qi)+","+workingTime(qi)+")");
        }
        db.update("TRUNCATE TABLE daily_log");
        db.disconnect();
    }

    public static void monthly(String date){
        ArrayList<Integer>ial_partial,ial_global=new ArrayList();
        int pieces=0;
        long total=0,duty=0;
        QueryResult qr=null,qi=null;
        String q_create = "CREATE table " + date + " ("
                + "machine_id INTEGER(255) NOT NULL,"
                + "pieces INTEGER(255) NOT NULL,"
                + "total_time INTEGER(255) NOT NULL,"
                + "working_time INTEGER(255) NOT NULL)";
        String q_insert="INSERT INTO "+date+" ";
        MySQLdb db = new MySQLdb("production", "root", "");
        db.connect();
        if(null==(qr=db.query("SELECT table_name FROM information_schema.tables WHERE table_name LIKE '"+date+"%'")))out.println("errore select like");
        if(!db.update(q_create))out.println("errore nuova tabella");
        //out.println(qr);
        

        //trovo tutti gli ID delle macchine che hanno lavorato almeno un giorno
        for(Record rcd:qr.getResultTable()){//giorno di questo mese
            if(null==(qi=db.query("SELECT * FROM "+rcd.getValueAt(0))))out.println("errore select id");//seleziono la tabella del i-giorno
            ial_partial=seekID(qi);
            
            for(Integer i:ial_partial){//vedi commento finale ***
                if(!ial_global.contains(i))
                    ial_global.add(i);
            }
        }
        
        //inserisco nel db i pezzi, il tempo di lavoro e il tempo utile 
        //delle macchine giorno per giorno 
        for(Integer i:ial_global){
            for(Record rcd:qr.getResultTable()){
                if(null==(qi=db.query("SELECT * FROM "+rcd.getValueAt(0)+" WHERE machine_id = "+i)))out.println("errore select parsing");
                for(Record rcd2:qi.getResultTable()){
                    pieces+=Integer.parseInt(rcd2.getValueAt(1));
                    total+=Long.parseLong(rcd2.getValueAt(2));
                    duty+=Long.parseLong(rcd2.getValueAt(3));
                }
            }
            //out.println("QUERY: "+q_insert+"VALUES("+i+","+pieces+","+total+","+duty+")");
            if(!db.update(q_insert+"VALUES("+i+","+pieces+","+total+","+duty+")"))out.println("errore insert");
            pieces=0;
            total=0;    //altro giro, altro regalo
            duty=0;
        }
        

        //cancello le tabelle,adesso non servono piu'
        for(Record rcd:qr.getResultTable()){
            if(!db.update("DROP TABLE "+rcd.getValueAt(0)))out.println("errore drop");
        }

        db.disconnect();
    }

    public static void annuary(String date){
        //NB: uguale in tutto e per tutto a monthly,
        //tranne che nelle query 'date' e' sempre preceduta da underscore
        ArrayList<Integer>ial_partial,ial_global=new ArrayList();
        int pieces=0;
        long total=0,duty=0;
        QueryResult qr=null,qi=null;
        String q_create = "CREATE table _" + date + " ("
                + "machine_id INTEGER(255) NOT NULL,"
                + "pieces INTEGER(255) NOT NULL,"
                + "total_time INTEGER(255) NOT NULL,"
                + "working_time INTEGER(255) NOT NULL)";
        String q_insert="INSERT INTO _"+date+" ";
        MySQLdb db = new MySQLdb("production", "root", "");
        db.connect();
        if(null==(qr=db.query("SELECT table_name FROM information_schema.tables WHERE table_name LIKE '"+date+"%'")))out.println("errore select like");
        if(!db.update(q_create))out.println("errore nuova tabella");
        //out.println(qr);
        

        //trovo tutti gli ID delle macchine che hanno lavorato almeno un giorno
        for(Record rcd:qr.getResultTable()){//giorno di questo mese
            if(null==(qi=db.query("SELECT * FROM "+rcd.getValueAt(0))))out.println("errore select id");//seleziono la tabella del i-giorno
            ial_partial=seekID(qi);
            
            for(Integer i:ial_partial){//vedi commento finale ***
                if(!ial_global.contains(i))
                    ial_global.add(i);
            }
        }
        
        //inserisco nel db i pezzi, il tempo di lavoro e il tempo utile 
        //delle macchine giorno per giorno 
        for(Integer i:ial_global){
            for(Record rcd:qr.getResultTable()){
                if(null==(qi=db.query("SELECT * FROM "+rcd.getValueAt(0)+" WHERE machine_id = "+i)))out.println("errore select parsing");
                for(Record rcd2:qi.getResultTable()){
                    pieces+=Integer.parseInt(rcd2.getValueAt(1));
                    total+=Long.parseLong(rcd2.getValueAt(2));
                    duty+=Long.parseLong(rcd2.getValueAt(3));
                }
            }
            //out.println("QUERY: "+q_insert+"VALUES("+i+","+pieces+","+total+","+duty+")");
            if(!db.update(q_insert+"VALUES("+i+","+pieces+","+total+","+duty+")"))out.println("errore insert");
            pieces=0;
            total=0;    //altro giro, altro regalo
            duty=0;
        }
        
        /*
        //cancello le tabelle,adesso non servono piu'
        for(Record rcd:qr.getResultTable()){
            if(!db.update("DROP TABLE "+rcd.getValueAt(0)))out.println("errore drop");
        }
        */
        db.disconnect();
    }

    private static ArrayList<Integer> seekID(QueryResult qr) {
        ArrayList<Integer> ial = new ArrayList<Integer>();
        for (Record rcd : qr.getResultTable()) {
            Integer n = Integer.parseInt(rcd.getValueAt(0));
            if (!ial.contains(n))
                ial.add(n);
        }
        return ial;
    }

    private static int countPieces(QueryResult qi) {
        Record act, prox;
        int pieces = 0;
        
        for(int j=0;j<qi.getResultTable().size()-1;j++){//size-1, l'ultimo record non ha un prox
            act = qi.getResultTable().get(j);
            prox = qi.getResultTable().get(j + 1);
            
            if(act.getValueAt(3).equals("WORKING") && prox.getValueAt(3).equals("WAITING"))
                pieces++;
        }
        return pieces;
    }
    
    private static long totalTime(QueryResult qr){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date d1=null,d2=null;
        CopyOnWriteArrayList<Record>cow=qr.getResultTable();
        long sum=0;
        
        try{
            d1 = sdf.parse(cow.get(0).getValueAt(4));
            d2 = sdf.parse(cow.get(cow.size()-1).getValueAt(4));
            sum+=DateHandler.diff(d1,d2);
        } catch (ParseException ex){
            out.println("ERRORE!!!");
        }
        return sum;
    }
    
    private static long workingTime(QueryResult qr){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date d1=null,d2=null;
        CopyOnWriteArrayList<Record>cow=qr.getResultTable();
        long sum=0;
        
        for(int i=0;i<cow.size()-1;i++)//size-1, l'ultimo record non ha un prox
            if(cow.get(i).getValueAt(3).equals("WORKING"))
                try {
                    d1=sdf.parse(cow.get(i).getValueAt(4));
                    d2=sdf.parse(cow.get(i+1).getValueAt(4));
                    sum+=DateHandler.diff(d1,d2);
                } catch (ParseException ex) {
                   out.println("ERRORE!!!");
                }
        return sum;
    }
}
/**
 * *** si puo' ottimizzare: nella tabella ogni record ha un'id differente,
 * quindi seekID risulta inutile e' piu' lenta. ma e' gia' fatta e non 
 * ho voglia di scriverne un'altra. tanto lo eseguo solo una volta al mese.
 * magari in fututo lo cambio
 */