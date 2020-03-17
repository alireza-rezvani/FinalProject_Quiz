package ir.maktab.arf.quiz.utilities;


/**
 * this enum contains titles of the statuses and their persian translation
 * @author Alireza
 */

public enum StatusTitle {
    ACTIVE("فعال"),
    INACTIVE("غیرفعال"),
    WAITING_FOR_VERIFY("در انتظار تایید");

    //adding statuses must be added in initializer too

    private String persian;
    StatusTitle(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}
