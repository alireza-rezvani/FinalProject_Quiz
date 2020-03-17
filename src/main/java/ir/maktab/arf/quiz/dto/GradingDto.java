package ir.maktab.arf.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * transfers default score and participant score for question item
 * @author Alireza
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradingDto {
    private Double defaultScoreForQuestion;
    private Double participantScoreForQuestion;
}
