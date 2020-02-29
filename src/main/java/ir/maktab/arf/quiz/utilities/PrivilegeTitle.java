package ir.maktab.arf.quiz.utilities;

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
