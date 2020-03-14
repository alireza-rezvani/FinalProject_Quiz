package ir.maktab.arf.quiz.utilities;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ScoresListTools {

    public static ArrayList<Double> stringToArrayList(String stringDefaultScores){
        ArrayList<Double> result = new ArrayList<>();
        if (stringDefaultScores != null) {
            for (String i : stringDefaultScores.split("-"))
                result.add(Double.parseDouble(i));
        }
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

    public static Double sum(String scores){
        if (scores == null || scores.split("-") == null || scores.split("-").length == 0)
            return 0.0;
        else
        return Stream.of(scores.split("-")).map(Double::parseDouble).mapToDouble(Double::doubleValue).sum();
    }
}
