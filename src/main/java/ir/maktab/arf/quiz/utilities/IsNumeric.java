package ir.maktab.arf.quiz.utilities;


/**
 * this class is used to recognize that a string is numeric or not
 * @author Alireza
 */

public class IsNumeric {
    public static boolean run(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}