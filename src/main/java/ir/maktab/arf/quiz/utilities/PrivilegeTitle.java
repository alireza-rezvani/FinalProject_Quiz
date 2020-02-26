package ir.maktab.arf.quiz.utilities;

public enum PrivilegeTitle {
    ACCOUNT_VERIFICATION("تایید حساب کاربری");

    private String persian;
    PrivilegeTitle(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}
