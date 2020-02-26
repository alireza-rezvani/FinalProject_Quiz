package ir.maktab.arf.quiz.utilities;

public enum StatusTitle {
    ACTIVE("فعال"),
    INACTIVE("غیرفعال");

    private String persian;
    StatusTitle(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}
