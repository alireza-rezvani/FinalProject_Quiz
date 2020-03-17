package ir.maktab.arf.quiz.dto;

import lombok.Data;


/**
 * transfers only question title to filter questions in a list
 * @author Alireza
 */

@Data
public class SearchQuestionDto {
    String title;
}
