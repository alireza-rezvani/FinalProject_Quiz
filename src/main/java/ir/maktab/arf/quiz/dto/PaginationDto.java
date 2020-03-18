package ir.maktab.arf.quiz.dto;

import lombok.Data;


/**
 * contains pagination requirements
 * @author Alireza
 */

@Data
public class PaginationDto {
    private String pageSize;
    private String sortBasedOn;
}
