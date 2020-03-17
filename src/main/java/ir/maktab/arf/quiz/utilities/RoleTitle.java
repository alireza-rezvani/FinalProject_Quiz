package ir.maktab.arf.quiz.utilities;


/**
 * this enum contains titles of the roles and their persian translation
 * @author Alireza
 */

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
