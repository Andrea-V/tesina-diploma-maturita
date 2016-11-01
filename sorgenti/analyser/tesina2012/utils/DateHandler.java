
package tesina2012.utils;

import java.util.Date;

/**
 *
 * @author andrea
 */
public class DateHandler {
    public static String[]explode(Date d,String pattern){
        String date=d.toString();
        date=date.replace('-','_');
        String[]exploded=date.split(pattern);
        return exploded;
    }
    public static long diff(Date d1,Date d2){
        return (d2.getTime()-d1.getTime())/1000;
    }
}
