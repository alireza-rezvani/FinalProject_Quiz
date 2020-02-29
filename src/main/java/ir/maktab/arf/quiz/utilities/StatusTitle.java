package ir.maktab.arf.quiz.utilities;

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
