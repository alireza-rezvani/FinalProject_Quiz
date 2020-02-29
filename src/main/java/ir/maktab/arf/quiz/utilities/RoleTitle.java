package ir.maktab.arf.quiz.utilities;

public enum RoleTitle {
    ADMIN("مدیر"),
    TEACHER("استاد"),
    STUDENT("دانشجو");

    //adding roles must be added in initializer too

    private String persian;
    RoleTitle(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}
