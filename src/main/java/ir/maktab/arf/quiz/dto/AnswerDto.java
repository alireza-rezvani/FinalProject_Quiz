package ir.maktab.arf.quiz.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private Long id;

    private String content;

    private Long questionId;

    private Integer questionNumberInQuiz;
}
