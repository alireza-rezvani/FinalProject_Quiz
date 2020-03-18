package ir.maktab.arf.quiz.utilities;

import org.joda.time.DateTime;
import org.kaveh.commons.farsi.utils.JalaliCalendarUtils;

import java.util.Date;


/**
 * this class is used for date processing
 * @author Alireza
 */

public class PersianDateTools {
    public static String gregorianDateToPersianString(Date gregorianDate){
        return JalaliCalendarUtils.convertGregorianToJalali(new DateTime(gregorianDate)).toString();
    }
}
