/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.domain;

import java.util.Date;

/**
 *
 * @author mikkoruuskanen
 */
public class Utils {
    public static Date getDateFromLong(Long aikaleima) {
        return new Date(aikaleima);
    }
    
    
    public static long getLongFromDate(Date date) {
        return date.getTime();
    }
}
