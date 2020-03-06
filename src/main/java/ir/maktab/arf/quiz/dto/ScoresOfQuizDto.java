package ir.maktab.arf.quiz.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScoresOfQuizDto {
    private List<Score> scores = new ArrayList<>();

    public void add(Score score){
        this.scores.add(score);
    }
}
