package ir.maktab.arf.quiz.utilities;


import java.util.ArrayList;
import java.util.List;

public class DefaultScoresTools {

    public static ArrayList<Double> stringToArrayList(String stringDefaultScores){
        ArrayList<Double> result = new ArrayList<>();
        for (String i : stringDefaultScores.split("-"))
            result.add(Double.parseDouble(i));
        return result;
    }

    public static String arrayListToString(ArrayList<Double> doubleScoresArrayList){
        String result = "";
        if (doubleScoresArrayList != null && doubleScoresArrayList.size() != 0){
            for (Double i : doubleScoresArrayList)
                result += i + "-";
        }
        return result;
    }
}
