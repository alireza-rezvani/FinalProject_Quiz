package ir.maktab.arf.quiz.utilities;

public enum StatusTitle {
    ACTIVE("فعال"),
    INACTIVE("غیرفعال"),
    WAITING_FOR_VERIFY("در انتظار تایید");

    private String persian;
    StatusTitle(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}
