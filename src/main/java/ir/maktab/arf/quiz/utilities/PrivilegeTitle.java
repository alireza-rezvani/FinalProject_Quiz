package ir.maktab.arf.quiz.utilities;


/**
 * this enum contains titles of the privileges and their persian translation
 * @author Alireza
 */

public enum PrivilegeTitle {
    ADMIN_GENERAL_PRIVILEGE("دسترسی کلی مدیر"),
    TEACHER_GENERAL_PRIVILEGE("دسترسی کلی استاد"),
    STUDENT_GENERAL_PRIVILEGE("دسترسی کلی دانشجو");

    //added privileges must be added in initializer too

    private String persian;
    PrivilegeTitle(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}
