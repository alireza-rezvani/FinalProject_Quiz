package ir.maktab.arf.quiz.utilities;

public enum QuestionType {
    DetailedQuestion("سوال تشریحی"),
    MultiChoiceQuestion("سوال تستی");

    //adding question types must be added here

    private String persian;
    QuestionType(String fa){
        persian = fa;
    }
    public String getPersian(){
        return persian;
    }
}
