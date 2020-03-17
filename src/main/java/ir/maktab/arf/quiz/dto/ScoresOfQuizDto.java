package ir.maktab.arf.quiz.dto;

import ir.maktab.arf.quiz.utilities.Score;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


/**
 * transfers scores
 * @author Alireza
 */

@Data
public class ScoresOfQuizDto {
    private List<Score> scores = new ArrayList<>();


    /**
     * adds a score to the list
     * @param score contains given score
     */

    public void add(Score score){
        this.scores.add(score);
    }
}
