package tesina2012;

import tesina2012.core.ConnectionManager;
import java.sql.Timestamp;
import tesina2012.utils.DateHandler;
import java.util.Date;
import tesina2012.crontask.Crontask;
import tesina2012.socket.DummyServer;
import static java.lang.System.out;

public class Main {

    public static ConnectionManager cm;

    public static void main(String[] args) {
        try {
            execCronCmd(args[0]);
        } catch (Exception ex) {
            normalExecution();
        }
    }

    private static void normalExecution() {
        //DummyServer ds = new DummyServer(7777);
        //ds.setDaemon(true);
        //ds.start();
        //*/

        cm = new ConnectionManager();
        cm.updateConnsTable();
        cm.startConnections();
        cm.createUserThread();
        cm.startManaging();
        //*/
    }

    /**
     * -d tutti i giorni
     * -w tutte le settimane
     * -m tutti i mesi
     * -y tutti gli anni
     */
    private static void execCronCmd(String cmd) {
        Timestamp ts = new Timestamp(new Date().getTime());
        String[] dp = DateHandler.explode(ts, " ");
        String[] dpp = dp[0].split("_");
        //out.println("Ciao mi ha chiamato crontab: " + cmd);

        if (cmd.charAt(0) != '-') {
            out.println("Errore, '" + cmd + "' non e' un'opzione valida.");
        } else {
            switch (cmd.charAt(1)) {
                case 'd':
                    Crontask.daily(dp[0]);
                    break;
                case 'm':
                    Crontask.monthly(dpp[0] + "_" + dpp[1]);
                    break;
                case 'y':
                    Crontask.annuary(dpp[0]);
                    break;
                default:
                    out.println("Errore, '" + cmd + "' non e' un'opzione valida.");
            }
        }
        //*/
    }
}
