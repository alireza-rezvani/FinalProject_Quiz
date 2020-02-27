package ir.maktab.arf.quiz.dto;

import lombok.Data;

@Data
public class SearchAccountDto {
    private String username;
    private String nationalCode;
    private String firstName;
    private String lastName;
    private String roleTitleName;
    private String statusTitleName;
}
