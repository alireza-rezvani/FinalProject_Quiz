package ir.maktab.arf.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

    private Long id;
    private String title;
    private String startDate;
    private String finishDate;

}
